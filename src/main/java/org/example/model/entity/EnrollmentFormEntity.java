package org.example.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import org.example.model.enums.EnrollmentFormStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollment_forms")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class EnrollmentFormEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private StudentEntity student;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    @JsonBackReference
    private AcademicYearEntity academicYear;

    @NonNull
    @Column(name = "semester", nullable = false)
    private Integer semester;

    @NonNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    @JsonBackReference
    private UserEntity approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "is_locked")
    private Boolean isLocked = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "enrollmentForm", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EnrollmentFormItemEntity> items = new ArrayList<>();

    public EnrollmentFormStatus getStatusEnum() {
        return EnrollmentFormStatus.fromValue(status);
    }
    public void setStatusEnum(EnrollmentFormStatus status) {
        this.status = status.getValue();
    }
}