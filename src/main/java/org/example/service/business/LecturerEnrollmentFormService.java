package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.repository.LecturerRepository;
import org.example.service.domain.EnrollmentFormDomainService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LecturerEnrollmentFormService {
    private final LecturerRepository lecturerRepository;
    private final EnrollmentFormDomainService enrollmentFormDomainService;

    public void approveFormAsLecturer(Long lecturerId, Long formId) {
        var lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(()->new RuntimeException("lecturer not found"));
        if (lecturer.getUser().getId() == null || lecturer.getId() == null) {
            throw new IllegalStateException("Lecturer not found");
        }

        enrollmentFormDomainService.approveForm(formId, lecturer.getId());

    }

    public void rejectFormAsLecturer(Long lecturerId, Long formId) {
        var lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(()->new RuntimeException("lecturer not found"));
        if (lecturer.getUser().getId() == null || lecturer.getId() == null) {
            throw new IllegalStateException("Lecturer not found");
        }
        enrollmentFormDomainService.rejectForm(formId, lecturer.getId());
    }
}
