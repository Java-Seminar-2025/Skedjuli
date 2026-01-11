package org.example.repository;

import org.example.model.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findByStudyProgram_IdAndSemesterAndMandatoryTrue(Long studyProgramId, Integer semester);

    List<CourseEntity> findByStudyProgram_IdAndSemesterAndMandatoryFalse(Long studyProgramId, Integer semester);

    boolean existsByCode(String code);

    List<CourseEntity> findByLecturer_Id(Long lecturerId);
}
