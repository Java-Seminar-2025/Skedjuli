package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.model.dto.response.EnrollmentFormItemReviewResponse;
import org.example.model.dto.response.EnrollmentFormLockedResponse;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.repository.EnrollmentFormItemRepository;
import org.example.repository.StudentRepository;
import org.example.service.domain.EnrollmentFormDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentEnrollmentFormService {
    private final StudentRepository studentRepository;
    private final EnrollmentFormDomainService enrollmentFormDomainService;
    private final EnrollmentFormItemRepository enrollmentFormItemRepository;

    public List<EnrollmentFormLockedResponse> getLockedForms(Long studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("student not found"));

        var lockedForms = enrollmentFormDomainService
                .findAllByStudentAndStatus(studentId, EnrollmentFormStatus.LOCKED.getValue());

        return lockedForms.stream().map(lockedForm -> {
            var items = enrollmentFormItemRepository.findByEnrollmentForm_Id(lockedForm.getId())
                    .stream()
                    .map(it -> {
                        var course = it.getCourse();
                        return new EnrollmentFormItemReviewResponse(
                                course != null ? course.getCode() : null,
                                course != null ? course.getName() : null,
                                course != null ? course.getEcts() : null,
                                it.getStatus()
                        );
                    }).toList();
            var user = student.getUser();
            return new EnrollmentFormLockedResponse(
                    lockedForm.getId(),
                    user != null ? user.getFirstName() : null,
                    user != null ? user.getLastName() : null,
                    user != null ? user.getUsername() : null,
                    lockedForm.getAcademicYear() != null ? lockedForm.getAcademicYear().getId() : null,
                    lockedForm.getSemester(),
                    lockedForm.getSubmittedAt(),
                    items
            );
        }).toList();
    }
}
