package org.example.repository;

import org.example.model.CompletedCourse;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletedCourseRepository extends JpaRepository<CompletedCourse, Long> {
    List<CompletedCourse> findByStudent(Student student);
}