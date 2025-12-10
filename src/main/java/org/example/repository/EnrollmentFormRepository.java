package org.example.repository;

import org.example.domain.entity.EnrollmentFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentFormRepository extends JpaRepository<EnrollmentFormEntity, Long> {
    Optional<EnrollmentFormEntity> findByStudent_IdAndSemester(Long studentId, Integer semester);
}