package org.example.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.entity.EnrollmentFormItemEntity;
import org.example.repository.EnrollmentFormItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@AllArgsConstructor
@Service
public class EnrollmentFormItemDomainService {
    private EnrollmentFormItemRepository enrollmentFormItemRepository;

    public List<EnrollmentFormItemEntity> getEnrollmentFormItems(Long enrollmentFormId) {
        return enrollmentFormItemRepository.findByEnrollmentForm_Id(enrollmentFormId);
    }
}
