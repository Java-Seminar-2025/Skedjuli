package org.example.repository;

import org.example.model.entity.EnrollmentFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentFormRepository extends JpaRepository<EnrollmentFormEntity, Long> {
    Optional<EnrollmentFormEntity> findTopByStudent_IdAndAcademicYear_IdAndSemesterOrderByIdDesc(Long studentId, Long yearId, int semester);
    Optional<EnrollmentFormEntity> findTopByStudent_IdAndSemesterOrderByIdDesc(Long studentId, int semester);
}