package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_req")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CourseRequirementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @NonNull
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "req_course_id", nullable = false)
    private CourseEntity requiredCourse;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}