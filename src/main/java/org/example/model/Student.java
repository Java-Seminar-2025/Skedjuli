package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public Student() {}

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getStudyProgramId() { return studyProgramId; }
    public void setStudyProgramId(Integer studyProgramId) { this.studyProgramId = studyProgramId; }

    public Integer getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(Integer enrollmentYear) { this.enrollmentYear = enrollmentYear; }

    public Integer getCurrentYear() { return currentYear; }
    public void setCurrentYear(Integer currentYear) { this.currentYear = currentYear; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
