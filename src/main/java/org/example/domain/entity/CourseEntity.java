package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "course_req",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "req_course_id")
    )
    private Set<CourseEntity> prerequisites = new HashSet<>();

    @ManyToMany(mappedBy = "prerequisites")
    private Set<CourseEntity> dependentCourses = new HashSet<>();
}