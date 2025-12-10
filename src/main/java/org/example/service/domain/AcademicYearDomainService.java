package org.example.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.repository.AcademicYearRepository;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@Service
public class AcademicYearDomainService {
    private final AcademicYearRepository academicYearRepository;

    public Long getActiveYearId() {
        return academicYearRepository.getByActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active year found"))
                .getId();
    }
}
