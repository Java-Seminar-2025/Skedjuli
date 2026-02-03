package org.example.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @NonNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NonNull
    @Column(name = "ects", nullable = false)
    private Integer ects;

    @Column(name = "is_mandatory")
    private Boolean mandatory = true;

    @Column(name = "enrollment_limit")
    private Integer enrollmentLimit;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    @JsonBackReference
    private LecturerEntity lecturer;

    @ManyToOne
    @JoinColumn(name = "study_program_id")
    @JsonBackReference
    private StudyProgramEntity studyProgram;

    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    @JsonBackReference
    private AcademicYearEntity academicYear;

    @NonNull
    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}