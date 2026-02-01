package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.model.dto.response.StudentResponse;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.repository.CompletedCourseRepository;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.EnrollmentFormItemDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LecturerCourseService {
    private final AcademicYearDomainService academicYearDomainService;
    private final EnrollmentFormItemDomainService enrollmentFormItemDomainService;
    private final CourseDomainService courseDomainService;
    private final CompletedCourseRepository completedCourseRepository;

    public List<StudentResponse> getStudentsForCourseLockedAndApproved(Long courseId) {
        var activeYear = academicYearDomainService.getActiveAcademicYear();
        if (activeYear == null) {
            throw new IllegalStateException("No active academic year found");
        }

        var statuses = List.of(
                EnrollmentFormStatus.LOCKED.getValue(),
                EnrollmentFormStatus.APPROVED.getValue()
        );

        var students = enrollmentFormItemDomainService
                .getStudentsForCourseInYearWithFormStatuses(courseId, activeYear.id(), statuses);
        return students.stream()
                .map(this::withComputedStats)
                .toList();
    }

    public long getStudentCountForCourseLockedAndApproved(Long courseId) {
        var activeYear = academicYearDomainService.getActiveAcademicYear();

        var statuses = List.of(
                EnrollmentFormStatus.LOCKED.getValue(),
                EnrollmentFormStatus.APPROVED.getValue()
        );
        return enrollmentFormItemDomainService.getStudentCountForCourseInYearWithFormStatuses(courseId, activeYear.id(), statuses);
    }

    public List<StudentResponse> getStudentsForCourseLockedAndApprovedForLecturer(
            Long lecturerId,
            Long courseId
    ) {
        if (lecturerId == null || courseId == null) return List.of();

        var courseOpt = courseDomainService.getCourse(courseId);
        if (courseOpt.isEmpty()) return List.of();

        var course = courseOpt.get();
        var courseLecturer = course.getLecturer();
        if (courseLecturer == null || courseLecturer.getId() == null) return List.of();

        if (!courseLecturer.getId().equals(lecturerId)) return List.of();

        var activeYear = academicYearDomainService.getActiveAcademicYear();
        if (activeYear == null) {
            throw new IllegalStateException("No active academic year found");
        }

        var statuses = List.of(
                EnrollmentFormStatus.LOCKED.getValue(),
                EnrollmentFormStatus.APPROVED.getValue()
        );

        var students = enrollmentFormItemDomainService
                .getStudentsForCourseInYearWithFormStatuses(courseId, activeYear.id(), statuses);

        return students.stream()
                .map(s -> {
                    if (s == null || s.id() == null) return s;

                    Double avgRaw = completedCourseRepository.avgOverallForStudent(s.id());
                    Double avg = avgRaw == null ? null : Math.round(avgRaw * 100.0) / 100.0;

                    Double ects = completedCourseRepository.totalEctsEarnedForStudent(s.id());

                    return new StudentResponse(
                            s.id(),
                            s.userId(),
                            s.user(),
                            s.studyProgramId(),
                            s.enrollmentYear(),
                            s.currentYear(),
                            avg,
                            ects,
                            s.isActive()
                    );
                })
                .toList();
    }


    public long getStudentCountForCourseLockedAndApprovedForLecturer(Long lecturerId, Long courseId) {
        if (lecturerId == null || courseId == null) return 0;

        var courseOpt = courseDomainService.getCourse(courseId);
        if (courseOpt.isEmpty()) return 0L;
        var course = courseOpt.get();
        var courseLecturer = course.getLecturer();
        if (courseLecturer == null || courseLecturer.getId() == null) return 0L;
        if (!courseLecturer.getId().equals(lecturerId)) return 0L;

        return getStudentCountForCourseLockedAndApproved(courseId);
    }

    private StudentResponse withComputedStats(StudentResponse s) {
        if (s==null || s.id()==null) return s;

        Double avg = normalize(completedCourseRepository.avgOverallForStudent(s.id()));
        Double ects = completedCourseRepository.totalEctsEarnedForStudent(s.id());

        return new StudentResponse(
                s.id(),
                s.userId(),
                s.user(),
                s.studyProgramId(),
                s.enrollmentYear(),
                s.currentYear(),
                avg,
                ects,
                s.isActive()
        );
    }

    private Double normalize(Double val) {
        if (val==null) return null;
        return Math.round(val*100.0)/100.0;
    }
}
