package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_form_items")
public class EnrollmentFormItem {

    public enum Status {
        PENDING(1),
        APPROVED(2),
        REJECTED(3);

        private final int value;

        Status(int value) { this.value = value; }
        public int getValue() { return value; }

        public static Status fromValue(int value) {
            return switch (value) {
                case 1 -> PENDING;
                case 2 -> APPROVED;
                case 3 -> REJECTED;
                default -> throw new IllegalArgumentException("Invalid status value: " + value);
            };
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_form_id", nullable = false)
    private EnrollmentForm enrollmentForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public EnrollmentFormItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EnrollmentForm getEnrollmentForm() { return enrollmentForm; }
    public void setEnrollmentForm(EnrollmentForm enrollmentForm) { this.enrollmentForm = enrollmentForm; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Status getStatus() { return Status.fromValue(status); }
    public void setStatus(Status status) { this.status = status.getValue(); }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
