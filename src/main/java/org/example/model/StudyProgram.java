package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "study_programs")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class StudyProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @NonNull
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NonNull
    @Column(name = "duration_years", nullable = false)
    private Integer durationYears;

    @Column(name = "total_ects")
    private Integer totalEcts;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();
}