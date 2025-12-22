package org.example.repository;

import org.example.model.entity.CompletedCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletedCourseRepository extends JpaRepository<CompletedCourseEntity, Long> {
    boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    List<CompletedCourseEntity> findByStudent_Id(Long studentId);
}