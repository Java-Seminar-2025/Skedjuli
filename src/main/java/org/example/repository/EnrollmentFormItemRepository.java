package org.example.repository;

import org.example.model.EnrollmentFormItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentFormItemRepository extends JpaRepository<EnrollmentFormItem, Long> {
}