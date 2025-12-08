package org.example.repository;

import org.example.domain.entity.AcademicYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity,Long> {
    Optional<AcademicYearEntity> getByActiveTrue();
}
