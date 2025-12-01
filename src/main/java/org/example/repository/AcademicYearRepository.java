package org.example.repository;

import org.example.model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicYearRepository extends JpaRepository<AcademicYear,Long> {
    AcademicYear findByActiveTrue();
}
