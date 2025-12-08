package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import org.example.domain.enums.EnrollmentFormItemStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_form_items")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class EnrollmentFormItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "enrollment_form_id", nullable = false)
    private EnrollmentFormEntity enrollmentForm;

    @NonNull
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @NonNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public EnrollmentFormItemStatus getStatusEnum() {
        return EnrollmentFormItemStatus.fromValue(status);
    }

    public void setStatusEnum(EnrollmentFormItemStatus statusEnum) {
        this.status = statusEnum.getValue();
    }
}