package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
@Table(name = "completed_courses")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CompletedCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private StudentEntity student;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private CourseEntity course;

    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    @JsonBackReference
    private AcademicYearEntity academicYear;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "created_at")
    private LocalDate createdAt;
}