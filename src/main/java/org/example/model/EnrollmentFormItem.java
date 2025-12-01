package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_form_items")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
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

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_form_id", nullable = false)
    private EnrollmentForm enrollmentForm;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NonNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Optional getters/setters for status
    public Status getStatusEnum() {
        return Status.fromValue(status);
    }

    public void setStatusEnum(Status statusEnum) {
        this.status = statusEnum.getValue();
    }
}