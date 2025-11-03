package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "study_program_id")
    public Integer studyProgramId;

    @Column(name = "enrollment_year")
    public Integer enrollmentYear;

    @Column(name = "current_year")
    public Integer currentYear;

    @Column(name = "is_active")
    public Boolean isActive = true;
}
