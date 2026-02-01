package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.model.dto.response.EnrollmentFormItemReviewResponse;
import org.example.model.dto.response.EnrollmentFormLockedResponse;
import org.example.model.entity.EnrollmentFormItemEntity;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.repository.EnrollmentFormItemRepository;
import org.example.repository.LecturerRepository;
import org.example.service.domain.EnrollmentFormDomainService;
import org.example.service.domain.EnrollmentFormItemDomainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class LecturerEnrollmentFormService {
    private final LecturerRepository lecturerRepository;
    private final EnrollmentFormDomainService enrollmentFormDomainService;
    private final EnrollmentFormItemRepository enrollmentFormItemRepository;

    public void approveFormAsLecturer(Long lecturerId, Long formId) {
        var lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(()->new RuntimeException("lecturer not found"));
        if (lecturer.getId() == null || lecturer.getUser().getId() == null) {
            throw new IllegalStateException("Lecturer not found");
        }

        enrollmentFormDomainService.approveForm(formId, lecturer.getUser().getId());

    }

    public void rejectFormAsLecturer(Long lecturerId, Long formId) {
        var lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(()->new RuntimeException("lecturer not found"));
        if (lecturer.getId() == null || lecturer.getUser().getId() == null) {
            throw new IllegalStateException("Lecturer not found");
        }
        enrollmentFormDomainService.rejectForm(formId, lecturer.getUser().getId());
    }

    public List<EnrollmentFormLockedResponse> getLockedForms(Long lecturerId) {
        lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("lecturer not found"));

        var locked = enrollmentFormDomainService
                .findByStatus(EnrollmentFormStatus.LOCKED.getValue());

        var formIds = locked.stream()
                .map(f -> f.getId())
                .toList();

        var allItems = formIds.isEmpty()
                ? List.<EnrollmentFormItemEntity>of()
                : enrollmentFormItemRepository.findByEnrollmentForm_IdIn(formIds);

        Map<Long, List<EnrollmentFormItemReviewResponse>> itemsByFormId = new HashMap<>();

        for (var it : allItems) {
            var form = it.getEnrollmentForm();
            if (form == null) continue;

            var course = it.getCourse();
            var dto = new EnrollmentFormItemReviewResponse(
                    course != null ? course.getCode() : null,
                    course != null ? course.getName() : null,
                    course != null ? course.getEcts() : null,
                    it.getStatus()
            );

            itemsByFormId
                    .computeIfAbsent(form.getId(), k -> new ArrayList<>())
                    .add(dto);
        }

        return locked.stream().map(f -> {
            var student = f.getStudent();
            var user = student != null ? student.getUser() : null;

            return new EnrollmentFormLockedResponse(
                    f.getId(),
                    user != null ? user.getFirstName() : null,
                    user != null ? user.getLastName() : null,
                    user != null ? user.getUsername() : null,
                    f.getAcademicYear() != null ? f.getAcademicYear().getId() : null,
                    f.getSemester(),
                    f.getSubmittedAt(),
                    itemsByFormId.getOrDefault(f.getId(), List.of())
            );
        }).toList();
    }

}
