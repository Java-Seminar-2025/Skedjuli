package org.example.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @NonNull
    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @NonNull
    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    @NonNull
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NonNull
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NonNull
    @Column(name = "role", nullable = false)
    private Integer role;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}