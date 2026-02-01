package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.model.dto.response.StudentResponse;
import org.example.model.enums.EnrollmentFormStatus;
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

    public List<StudentResponse> getStudentsForCourseLockedAndApproved(Long courseId) {
        var activeYear = academicYearDomainService.getActiveAcademicYear();
        if (activeYear == null) {
            throw new IllegalStateException("No active academic year found");
        }

        var statuses = List.of(
                EnrollmentFormStatus.LOCKED.getValue(),
                EnrollmentFormStatus.APPROVED.getValue()
        );

        return enrollmentFormItemDomainService.getStudentsForCourseInYearWithFormStatuses(courseId, activeYear.id(), statuses);
    }

    public long getStudentCountForCourseLockedAndApproved(Long courseId) {
        var activeYear = academicYearDomainService.getActiveAcademicYear();

        var statuses = List.of(
                EnrollmentFormStatus.LOCKED.getValue(),
                EnrollmentFormStatus.APPROVED.getValue()
        );
        return enrollmentFormItemDomainService.getStudentCountForCourseInYearWithFormStatuses(courseId, activeYear.id(), statuses);
    }

    public List<StudentResponse> getStudentsForCourseLockedAndApprovedForLecturer(Long lecturerId, Long courseId) {
        if (lecturerId == null || courseId == null) return List.of();

        var courseOpt = courseDomainService.getCourse(courseId);
        if (courseOpt.isEmpty()) {
            return List.of();
        }

        var course = courseOpt.get();
        var courseLecturer = course.getLecturer();
        if (courseLecturer == null || courseLecturer.getId() == null) return List.of();

        if (!courseLecturer.getId().equals(lecturerId)) {
            return List.of();
        }

        return getStudentsForCourseLockedAndApproved(courseId);
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
}
