package org.example.repository;

import org.example.model.EnrollmentForm;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentFormRepository extends JpaRepository<EnrollmentForm, Long> {

    Optional<Object> findByStudentAndSemester(Student student, int semester);

    List<EnrollmentForm> findByStudent(Student student);
}