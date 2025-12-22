package org.example.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.model.entity.AcademicYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity,Long> {
    Optional<AcademicYearEntity> getByActiveTrue();

    boolean existsByYearCode(String yearCode);
}
