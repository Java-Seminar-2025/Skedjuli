package org.example.repository;

import org.example.model.entity.EnrollmentFormEntity;
import org.example.model.enums.EnrollmentFormStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentFormRepository extends JpaRepository<EnrollmentFormEntity, Long> {
    Optional<EnrollmentFormEntity> findTopByStudent_IdAndAcademicYear_IdAndSemesterOrderByIdDesc(Long studentId, Long yearId, int semester);
    Optional<EnrollmentFormEntity> findTopByStudent_IdAndSemesterOrderByIdDesc(Long studentId, int semester);

    List<EnrollmentFormEntity> findAllByStudent_IdAndStatusOrderByCreatedAtDesc(Long studentId, int status);

    List<EnrollmentFormEntity> findAllByStatusOrderBySubmittedAtDesc(Integer status);
}