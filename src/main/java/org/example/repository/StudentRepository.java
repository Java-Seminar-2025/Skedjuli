package org.example.repository;

import org.example.model.Student;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
}
