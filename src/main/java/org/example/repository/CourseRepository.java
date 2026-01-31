package org.example.repository;

import org.example.model.entity.CourseEntity;
import org.example.model.entity.StudyProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findByStudyProgram_IdAndSemesterAndMandatoryTrue(Long studyProgramId, Integer semester);

    List<CourseEntity> findByStudyProgram_IdAndSemesterAndMandatoryFalse(Long studyProgramId, Integer semester);

    boolean existsByCode(String code);

    List<CourseEntity> findByLecturer_Id(Long lecturerId);

    @Query("select distinct c.studyProgram from CourseEntity c where c.lecturer.id = :lecturerId and c.academicYear.active = true and c.studyProgram.active = true")
    List<StudyProgramEntity> findDistinctStudyProgramByLecturerId(@Param("lecturerId") Long lecturerId);
}
