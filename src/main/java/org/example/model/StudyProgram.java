package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "study_programs")
public class StudyProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_years", nullable = false)
    private Integer durationYears;

    @Column(name = "total_ects")
    private Integer totalEcts;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    // Constructors

    public StudyProgram() {}

    // Getters & Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDurationYears() { return durationYears; }
    public void setDurationYears(Integer durationYears) { this.durationYears = durationYears; }

    public Integer getTotalEcts() { return totalEcts; }
    public void setTotalEcts(Integer totalEcts) { this.totalEcts = totalEcts; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Set<Course> getCourses() { return courses; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
