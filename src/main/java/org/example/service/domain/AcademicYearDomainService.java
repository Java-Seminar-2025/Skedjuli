package org.example.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.entity.AcademicYearEntity;
import org.example.repository.AcademicYearRepository;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@Service
public class AcademicYearDomainService {
    private final AcademicYearRepository academicYearRepository;

    public AcademicYearEntity getActiveYear() {
        return academicYearRepository.getByActiveTrue().orElseThrow(() -> new RuntimeException("No active year found"));
    }
}