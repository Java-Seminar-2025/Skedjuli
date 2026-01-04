package org.example.repository;

import org.example.model.entity.EnrollmentFormItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentFormItemRepository extends JpaRepository<EnrollmentFormItemEntity, Long> {
    List<EnrollmentFormItemEntity> findByEnrollmentForm_Id(Long enrollmentFormId);
}