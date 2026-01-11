package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.model.dto.response.CourseResponse;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.domain.CompletedCourseDomainService;
import org.example.service.domain.EnrollmentFormItemDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class StudentCourseService {

    private final AcademicYearDomainService academicYearDomainService;
    private final CompletedCourseDomainService completedCourseDomainService;
    private final EnrollmentFormItemDomainService enrollmentFormItemDomainService;

    public List<CourseResponse> getEnrolledCourses(Long studentId) {

        var activeYear = academicYearDomainService.getActiveAcademicYear();

        if (activeYear == null) {
            throw new IllegalStateException("No active academic year found");
        }

        Set<Long> completedCourseIds = completedCourseDomainService.getCompletedCourseIdSet(studentId);

        return enrollmentFormItemDomainService.findEnrolledCoursesForStudent(
                        studentId,
                        EnrollmentFormStatus.APPROVED.getValue(),
                        activeYear.id()
                )
                .stream()
                .filter(c -> !completedCourseIds.contains(c.id()))
                .toList();
    }
}
