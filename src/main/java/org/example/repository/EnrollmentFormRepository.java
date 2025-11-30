package org.example.repository;

import org.example.model.EnrollmentForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentFormRepository extends JpaRepository<EnrollmentForm, Long> {

    Optional<Object> findByStudentIdAndSemester(Long id, int semester);

    List<EnrollmentForm> findByStudentId(Long studentId);
}