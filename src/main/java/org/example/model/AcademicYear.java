package org.example.model;

import jakarta.persistence.*;
import lombok.*;

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
public class AcademicYear {

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
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "academicYear", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();
}