package org.example.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "academic_years")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class AcademicYearEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "year_code", nullable = false, unique = true, length = 20)
    private String yearCode;

    @NonNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NonNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "enrollment_start")
    private LocalDate enrollmentStart;

    @Column(name = "enrollment_end")
    private LocalDate enrollmentEnd;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "academicYear")
    @JsonManagedReference
    private Set<CourseEntity> courses = new HashSet<>();
}