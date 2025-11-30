package org.example.repository;

import org.example.model.CompletedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletedCourseRepository extends JpaRepository<CompletedCourse, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Long> findCourseIdsByStudentId(Long id);
}