package org.example.repository;

import org.example.model.entity.CompletedCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompletedCourseRepository extends JpaRepository<CompletedCourseEntity, Long> {
    Optional<CompletedCourseEntity> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    List<CompletedCourseEntity> findByStudent_Id(Long studentId);

    @Query("""
        SELECT AVG(cc.grade)
        FROM CompletedCourseEntity cc
        WHERE cc.student.id = :studentId
          AND cc.grade IS NOT NULL
    """)
    Double avgOverallForStudent(@Param("studentId") Long studentId);

    @Query("""
        SELECT AVG(cc.grade)
        FROM CompletedCourseEntity cc
        WHERE cc.student.id = :studentId
          AND cc.academicYear.id = :academicYearId
          AND cc.grade IS NOT NULL
    """)
    Double avgForStudentInAcademicYear(
            @Param("studentId") Long studentId,
            @Param("academicYearId") Long academicYearId
    );

    @Query("""
        SELECT AVG(cc.grade)
        FROM CompletedCourseEntity cc
        WHERE cc.student.studyProgram.id = :studyProgramId
          AND cc.student.enrollmentYear = :enrollmentYear
          AND cc.grade IS NOT NULL
    """)
    Double avgOverallForCohort(
            @Param("studyProgramId") Long studyProgramId,
            @Param("enrollmentYear") Integer enrollmentYear
    );

    @Query("""
        SELECT AVG(cc.grade)
        FROM CompletedCourseEntity cc
        WHERE cc.student.studyProgram.id = :studyProgramId
          AND cc.student.enrollmentYear = :enrollmentYear
          AND cc.academicYear.id = :academicYearId
          AND cc.grade IS NOT NULL
    """)
    Double avgForCohortInAcademicYear(
            @Param("studyProgramId") Long studyProgramId,
            @Param("enrollmentYear") Integer enrollmentYear,
            @Param("academicYearId") Long academicYearId
    );
}