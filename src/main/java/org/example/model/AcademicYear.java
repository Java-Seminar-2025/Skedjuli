package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "academic_years")
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year_code", nullable = false, unique = true, length = 20)
    private String yearCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "enrollment_start")
    private LocalDate enrollmentStart;

    @Column(name = "enrollment_end")
    private LocalDate enrollmentEnd;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "academicYear", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    // Constructors

    public AcademicYear() {}

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getYearCode() { return yearCode; }
    public void setYearCode(String yearCode) { this.yearCode = yearCode; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getEnrollmentStart() { return enrollmentStart; }
    public void setEnrollmentStart(LocalDate enrollmentStart) { this.enrollmentStart = enrollmentStart; }

    public LocalDate getEnrollmentEnd() { return enrollmentEnd; }
    public void setEnrollmentEnd(LocalDate enrollmentEnd) { this.enrollmentEnd = enrollmentEnd; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<Course> getCourses() { return courses; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }
}
