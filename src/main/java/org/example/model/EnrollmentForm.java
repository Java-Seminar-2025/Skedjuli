package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "enrollment_forms")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class EnrollmentForm {
    public enum Status {
        PENDING(1),
        APPROVED(2),
        REJECTED(3),
        LOCKED(4);

        private final int value;

        Status(int value) { this.value = value; }
        public int getValue() { return value; }

        public static Status fromValue(int value) {
            return switch (value) {
                case 1 -> PENDING;
                case 2 -> APPROVED;
                case 3 -> REJECTED;
                case 4 -> LOCKED;
                default -> throw new IllegalArgumentException("Invalid status value: " + value);
            };
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @NonNull
    @Column(name = "semester", nullable = false)
    private Integer semester;

    @NonNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "is_locked")
    private Boolean isLocked = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "enrollmentForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnrollmentFormItem> items = new ArrayList<>();

    // --- Enum access ---
    public Status getStatusEnum() {
        return Status.fromValue(status);
    }
    public void setStatusEnum(Status status) {
        this.status = status.getValue();
    }
}