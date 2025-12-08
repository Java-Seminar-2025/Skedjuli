package org.example.repository;

import org.example.domain.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findByStudyProgram_IdAndSemesterAndMandatoryTrue(Long studyProgramId, Integer semester);
}
