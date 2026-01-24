package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.model.dto.response.StudentResponse;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.domain.EnrollmentFormItemDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LecturerCourseService {
    private final AcademicYearDomainService academicYearDomainService;
    private final EnrollmentFormItemDomainService enrollmentFormItemDomainService;

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
}
