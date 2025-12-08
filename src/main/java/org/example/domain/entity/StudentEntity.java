package org.example.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "study_program_id", nullable = false, unique = true)
    private StudyProgramEntity studyProgram;

    @NonNull
    @Column(name = "enrollment_year",  nullable = false)
    private Integer enrollmentYear;

    @NonNull
    @Column(name = "current_year" , nullable = false)
    private Integer currentYear;

    @Column(name = "average_grade")
    private Double averageGrade;

    @Column(name = "total_ects_earned")
    private Double totalEctsEarned;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}