package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_req")
public class CourseRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_course_id", nullable = false)
    private Course requiredCourse;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CourseRequirement() {}

    public CourseRequirement(Course course, Course requiredCourse) {
        this.course = course;
        this.requiredCourse = requiredCourse;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Course getRequiredCourse() { return requiredCourse; }
    public void setRequiredCourse(Course requiredCourse) { this.requiredCourse = requiredCourse; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
