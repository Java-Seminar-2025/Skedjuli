package org.example.service.business;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.response.CourseResponse;
import org.example.model.dto.response.EnrollmentFormHistoryResponse;
import org.example.model.enums.YearTarget;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.domain.CompletedCourseDomainService;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.EnrollmentFormDomainService;
import org.example.service.domain.EnrollmentFormItemDomainService;
import org.example.service.domain.StudentDomainService;
import org.example.service.validator.EnrollmentSelectionValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Getter
@AllArgsConstructor
public class EnrollmentService {

    private final CourseDomainService courseDomainService;
    private final StudentDomainService studentDomainService;
    private final AcademicYearDomainService academicYearDomainService;
    private final EnrollmentFormDomainService enrollmentFormDomainService;
    private final CompletedCourseDomainService completedCourseDomainService;
    private final EnrollmentFormItemDomainService enrollmentFormItemDomainService;
    private final EnrollmentSelectionValidator enrollmentSelectionValidator;

    public void enrollYear(Long studentId) {
        enrollYear(studentId, List.of(), false);
    }

    @Transactional
    public void enrollYear(Long studentId, List<Long> selectedCourseIds, boolean allowHigherYearSelection) {
        enrollmentSelectionValidator.validateEnrollment(studentId, selectedCourseIds, allowHigherYearSelection);

        var completedSet = completedCourseDomainService.getCompletedCourseIdSet(studentId);
        var year = studentDomainService.getCurrentYearById(studentId);
        var studyProgramId = studentDomainService.getStudyProgramIdByStudentId(studentId);
        var activeYearId = academicYearDomainService.getActiveAcademicYear().id();
        var semStart = (year - 1) * 2 + 1;

        var candidates = getCoursesToConsider(studentId, studyProgramId, year);

        var selectedCourseResponses = Optional.ofNullable(selectedCourseIds)
                .stream()
                .flatMap(List::stream)
                .distinct()
                .map(courseDomainService::getCourseById)
                .distinct()
                .toList();

        var selectedIds = selectedCourseResponses.stream().map(CourseResponse::id).collect(Collectors.toSet());

        var mergedCandidates = Stream.concat(selectedCourseResponses.stream(), candidates.stream())
                .filter(c -> !completedSet.contains(c.id()))
                .distinct()
                .sorted(Comparator
                        .comparing((CourseResponse c) -> selectedIds.contains(c.id())).reversed()
                        .thenComparingInt(CourseResponse::ects).reversed())
                .toList();

        var range = YearTarget.fromYear(year).getRange(false);

        allocateYearCourses(
                studentId,
                activeYearId,
                semStart,
                semStart + 1,
                mergedCandidates,
                selectedIds,
                range.min(),
                range.max()
        );
    }

    private List<CourseResponse> getCoursesToConsider(Long studentId, Long studyProgramId, int year) {
        var completedSet =  completedCourseDomainService.getCompletedCourseIdSet(studentId);
        return switch (year) {
            case 1 -> courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(1, 2)).stream()
                    .filter(c -> !completedSet.contains(c.id()))
                    .toList();
            case 2 -> {
                var year1 = courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(1, 2));
                var missingYear1 = year1.stream()
                        .filter(c -> !completedSet.contains(c.id()))
                        .toList();
                var year2 = courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(3, 4)).stream()
                        .filter(c -> !completedSet.contains(c.id()))
                        .toList();
                yield Stream.concat(missingYear1.stream(), year2.stream()).toList();
            }
            case 3 -> {
                var remaining = getRemainingMandatoryForStudent(studentId, studyProgramId);
                var selectives = courseDomainService.getSelectableCoursesForYear(studyProgramId, 3);
                var combined = new ArrayList<CourseResponse>();
                combined.addAll(remaining);
                combined.addAll(selectives);
                yield combined;
            }
            default -> throw new RuntimeException("Unsupported year: " + year);
        };
    }

    private List<CourseResponse> getRemainingMandatoryForStudent(Long studentId, Long studyProgramId) {
        var allMandatory = courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(1, 2, 3, 4, 5, 6));
        var completedSet = completedCourseDomainService.getCompletedCourseIdSet(studentId);
        return allMandatory.stream()
                .filter(c -> !completedSet.contains(c.id()))
                .toList();
    }

    private void allocateYearCourses(
            Long studentId,
            Long activeYearId,
            int sem1,
            int sem2,
            List<CourseResponse> candidates,
            Set<Long> selectedIds,
            int minTarget,
            int maxTarget
    ) {
        var form1Id = enrollmentFormDomainService.findOrCreateFormId(studentId, activeYearId, sem1);
        var form2Id = enrollmentFormDomainService.findOrCreateFormId(studentId, activeYearId, sem2);

        ensureFormModifiable(form1Id);
        ensureFormModifiable(form2Id);

        var existing1 = enrollmentFormItemDomainService.getEnrollmentFormItems(form1Id)
                .stream()
                .map(CourseResponse::id)
                .toList();
        enrollmentFormItemDomainService.deleteByFormAndCourseIds(form1Id, existing1);

        var existing2 = enrollmentFormItemDomainService.getEnrollmentFormItems(form2Id)
                .stream()
                .map(CourseResponse::id)
                .toList();
        enrollmentFormItemDomainService.deleteByFormAndCourseIds(form2Id, existing2);

        var completedSet = completedCourseDomainService.getCompletedCourseIdSet(studentId);

        var toConsider = candidates.stream()
                .filter(c -> !completedSet.contains(c.id()))
                .distinct()
                .sorted(Comparator
                        .comparing((CourseResponse c) -> selectedIds.contains(c.id())).reversed()
                        .thenComparingInt(CourseResponse::ects).reversed())
                .toList();

        var semToForm = Map.of(sem1, form1Id, sem2, form2Id);

        var load1 = new AtomicInteger(enrollmentFormItemDomainService.getTotalEctsForForm(form1Id));
        var load2 = new AtomicInteger(enrollmentFormItemDomainService.getTotalEctsForForm(form2Id));
        var total = new AtomicInteger(0);

        toConsider.stream()
                .takeWhile(c -> total.get() < minTarget)
                .forEach(course -> {
                    var potentialTotal = total.get() + course.ects();
                    Optional.of(potentialTotal)
                            .filter(t -> t <= maxTarget)
                            .ifPresent(t -> {
                                var targetFormId = Optional.ofNullable(semToForm.get(course.semester()))
                                        .orElseGet(() -> (load1.get() <= load2.get()) ? form1Id : form2Id);

                                enrollmentFormDomainService.addItemByFormId(targetFormId, course.id());

                                Optional.of(targetFormId.equals(form1Id))
                                        .filter(Boolean::booleanValue)
                                        .ifPresentOrElse(
                                                b -> load1.addAndGet(course.ects()),
                                                () -> load2.addAndGet(course.ects())
                                        );

                                total.addAndGet(course.ects());
                            });
                });
        if (total.get() < minTarget) {
            throw new IllegalStateException("Not enough eligible courses to reach minimum ECTS target.");
        }
    }

    public List<CourseResponse> getEnrolledCoursesForYear(Long studentId) {
        var year = studentDomainService.getCurrentYearById(studentId);
        var activeYearId = academicYearDomainService.getActiveAcademicYear().id();

        var sem1 = year * 2 - 1;
        var sem2 = year * 2;

        var formIds = Stream.of(sem1, sem2)
                .map(sem -> enrollmentFormDomainService
                        .findFormIdByStudentAndAcademicYearAndSemester(studentId, activeYearId, sem)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return formIds.stream()
                .flatMap(formId -> enrollmentFormItemDomainService.getEnrollmentFormItems(formId).stream())
                .toList();
    }

    public Long saveSelection(Long studentId, List<Long> selectedCourseIds, boolean allowHigherYearSelection) {
        enrollmentSelectionValidator.validateEnrollment(studentId, selectedCourseIds, allowHigherYearSelection);
        return createOrUpdateSelectionForStudent(studentId, selectedCourseIds);
    }

    public List<CourseResponse> getSelection(Long studentId) {
        return Optional.ofNullable(getCurrentEnrollmentFormId(studentId))
                .map(enrollmentFormItemDomainService::getEnrollmentFormItems)
                .orElse(List.of());
    }

    public Long getCurrentEnrollmentFormId(Long studentId) {
        var year = studentDomainService.getCurrentYearById(studentId);
        var activeYearId = academicYearDomainService.getActiveAcademicYear().id();
        var semStart = (year - 1) * 2 + 1;

        return enrollmentFormDomainService.findCurrentFormIdForStudent(studentId, activeYearId, semStart)
                .orElse(null);
    }

    @Transactional
    public Long createOrUpdateSelectionForStudent(Long studentId, List<Long> selectedCourseIds) {
        if (selectedCourseIds == null || selectedCourseIds.isEmpty()) {
            var year = studentDomainService.getCurrentYearById(studentId);
            var activeYearId = academicYearDomainService.getActiveAcademicYear().id();
            var semStart = (year - 1) * 2 + 1;
            return enrollmentFormDomainService.findOrCreateFormId(studentId, activeYearId, semStart);
        }
        var year = studentDomainService.getCurrentYearById(studentId);
        var activeYearId = academicYearDomainService.getActiveAcademicYear().id();
        var semStart = (year - 1) * 2 + 1;
        var sems = List.of(semStart, semStart + 1);

        var selectedDistinct = Optional.ofNullable(selectedCourseIds)
                .stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        var courseById = selectedDistinct.stream()
                .map(courseDomainService::getCourseById)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(CourseResponse::id, c -> c));

        var bySem = courseById.values().stream()
                .filter(ci -> sems.contains(ci.semester()))
                .collect(Collectors.groupingBy(
                        CourseResponse::semester,
                        Collectors.mapping(CourseResponse::id, Collectors.toList())
                ));

        sems.forEach(sem -> {
            var formId = enrollmentFormDomainService.findOrCreateFormId(studentId, activeYearId, sem);
            ensureFormModifiable(formId);

            var existingCourseIds = enrollmentFormItemDomainService.getEnrollmentFormItems(formId).stream()
                    .map(CourseResponse::id)
                    .collect(Collectors.toSet());

            var targetCourseIds = Optional.ofNullable(bySem.get(sem)).orElse(List.of());

            var toAddIds = targetCourseIds.stream()
                    .filter(id -> !existingCourseIds.contains(id))
                    .toList();

            var toRemoveIds = existingCourseIds.stream()
                    .filter(id -> !targetCourseIds.contains(id))
                    .toList();

            Optional.of(toRemoveIds)
                    .filter(l -> !l.isEmpty())
                    .ifPresent(l -> enrollmentFormItemDomainService.deleteByFormAndCourseIds(formId, l));

            Optional.of(toAddIds)
                    .filter(l -> !l.isEmpty())
                    .ifPresent(l -> enrollmentFormItemDomainService.saveItemsForForm(formId, l));
        });

        return enrollmentFormDomainService.findOrCreateFormId(studentId, activeYearId, semStart);
    }

    private void ensureFormModifiable(Long formId) {
        if (enrollmentFormDomainService.isLocked(formId)) {
            throw new IllegalStateException("Enrollment form is locked and cannot be modified");
        }
    }

    public List<EnrollmentFormHistoryResponse> getApprovedEnrollmentHistory(Long studentId) {
        return enrollmentFormDomainService.getApprovedFormsHistory(studentId).stream()
                .map(f -> new EnrollmentFormHistoryResponse(
                        f.getId(),
                        f.getAcademicYear().getId(),
                        f.getAcademicYear().getYearCode(),
                        f.getSemester(),
                        f.getApprovedAt(),
                        Boolean.TRUE.equals(f.getIsLocked()),
                        f.getCreatedAt()
                ))
                .toList();
    }

}
