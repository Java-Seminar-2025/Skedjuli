package org.example.service.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.dto.CourseInfo;
import org.example.domain.dto.EnrollmentCourseResponse;
import org.example.service.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public void enrollYear(Long studentId) {
        var year = studentDomainService.getCurrentYearById(studentId);
        if (year < 1 || year > 3) throw new RuntimeException("Unsupported study year: " + year);

        var studyProgramId = studentDomainService.getStudyProgramIdByStudentId(studentId);
        var activeYearId = academicYearDomainService.getActiveYearId();

        var semStart = (year - 1) * 2 + 1;
        var candidates = getCoursesToConsider(studentId, studyProgramId, year);
        var minMax = getTargetsForYear(year, false); // TODO: detect final paper
        allocateYearCourses(studentId, activeYearId, semStart, semStart + 1, candidates, minMax[0], minMax[1]);
    }

    private int[] getTargetsForYear(int year, boolean hasFinalPaper) {
        return switch (year) {
            case 1 -> new int[]{60, 60};
            case 2 -> new int[]{58, 62};
            case 3 -> new int[]{58, hasFinalPaper ? 80 : 62};
            default -> throw new RuntimeException("Unsupported year: " + year);
        };
    }

    private List<CourseInfo> getCoursesToConsider(Long studentId, Long studyProgramId, int year) {
        return switch (year) {
            case 1 -> courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(1, 2)).stream()
                    .filter(c -> !completedCourseDomainService.hasCompletedCourse(studentId, c.id()))
                    .collect(Collectors.toList());
            case 2 -> {
                var year1 = courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(1, 2));
                var missingYear1 = year1.stream()
                        .filter(c -> !completedCourseDomainService.hasCompletedCourse(studentId, c.id()))
                        .toList();
                var year2 = courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(3, 4));
                yield Stream.concat(missingYear1.stream(), year2.stream()).collect(Collectors.toList());
            }
            case 3 -> {
                var remaining = getRemainingMandatoryForStudent(studentId, studyProgramId);
                var selectives = courseDomainService.getSelectableCoursesForYear(studyProgramId, 3);
                var combined = new ArrayList<CourseInfo>();
                combined.addAll(remaining);
                combined.addAll(selectives);
                yield combined;
            }
            default -> throw new RuntimeException("Unsupported year: " + year);
        };
    }

    private List<CourseInfo> getRemainingMandatoryForStudent(Long studentId, Long studyProgramId) {
        var allMandatory = courseDomainService.getMandatoryCoursesForSemesters(studyProgramId, List.of(1,2,3,4,5,6));
        return allMandatory.stream()
                .filter(c -> !completedCourseDomainService.hasCompletedCourse(studentId, c.id()))
                .collect(Collectors.toList());
    }

    private void allocateYearCourses(Long studentId,
                                     Long activeYearId,
                                     int sem1,
                                     int sem2,
                                     List<CourseInfo> candidates,
                                     int minTarget,
                                     int maxTarget) {

        var form1Id = enrollmentFormDomainService.createEmptyFormReturnId(studentId, activeYearId, sem1);
        var form2Id = enrollmentFormDomainService.createEmptyFormReturnId(studentId, activeYearId, sem2);

        var toConsider = candidates.stream()
                .filter(c -> !completedCourseDomainService.hasCompletedCourse(studentId, c.id()))
                .distinct()
                .sorted(Comparator.comparingInt(CourseInfo::ects).reversed())
                .toList();

        var total = 0;
        for (var course : toConsider) {
            if (total >= minTarget) break;
            var potentialTotal = total + course.ects();
            if (potentialTotal > maxTarget && total >= minTarget) continue;

            Long targetFormId;
            if (course.semester() == sem1) targetFormId = form1Id;
            else if (course.semester() == sem2) targetFormId = form2Id;
            else {
                var load1 = enrollmentFormItemDomainService.getTotalEctsForForm(form1Id);
                var load2 = enrollmentFormItemDomainService.getTotalEctsForForm(form2Id);
                targetFormId = load1 <= load2 ? form1Id : form2Id;
            }

            enrollmentFormDomainService.addItemByFormId(targetFormId, course.id());
            total += course.ects();
        }
    }

    public List<EnrollmentCourseResponse> getEnrolledCoursesForYear(Long studentId) {
        var year = studentDomainService.getCurrentYearById(studentId);
        var sem1 = year * 2 - 1;
        var sem2 = year * 2;

        var formIds = List.of(
                enrollmentFormDomainService.getEnrollmentFormId(studentId, sem1),
                enrollmentFormDomainService.getEnrollmentFormId(studentId, sem2)
        );

        return formIds.stream()
                .flatMap(formId -> enrollmentFormItemDomainService.getEnrollmentFormItems(formId).stream())
                .collect(Collectors.toList());
    }
}
