package org.example.repository;

import org.example.model.dto.response.CourseResponse;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.EnrollmentFormItemEntity;
import org.example.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentFormItemRepository extends JpaRepository<EnrollmentFormItemEntity, Long> {
    List<EnrollmentFormItemEntity> findByEnrollmentForm_Id(Long enrollmentFormId);

    @Query("""
        SELECT DISTINCT c
        FROM EnrollmentFormItemEntity efi
        JOIN efi.enrollmentForm ef
        JOIN efi.course c
        WHERE ef.student.id = :studentId
          AND ef.status = :approvedStatus
          AND ef.academicYear.id = :academicYearId
    """)
    List<CourseEntity> findEnrolledCoursesForStudent(
            @Param("studentId") Long studentId,
            @Param("approvedStatus") Integer approvedStatus,
            @Param("academicYearId") Long academicYearId
    );

    @Query("""

    SELECT DISTINCT s
    FROM EnrollmentFormItemEntity efi
    JOIN efi.enrollmentForm ef
    JOIN ef.student s
    WHERE efi.course.id = :courseId
        AND ef.academicYear.id = :academicYearId
        AND ef.status IN :statuses
    """)

    List<StudentEntity> findDistinctStudentsByCourseAndYearAndFormStatuses (
            @Param("courseId") Long courseId,
            @Param("academicYearId") Long academicYearId,
            @Param("statuses") List<Integer> statuses
    );
}