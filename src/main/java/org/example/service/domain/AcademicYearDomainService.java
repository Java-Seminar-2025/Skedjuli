package org.example.service.domain;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.AcademicYearCreateRequest;
import org.example.model.dto.AcademicYearDto;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.mapper.AcademicYearMapper;
import org.example.repository.AcademicYearRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Service
public class AcademicYearDomainService {
    private final AcademicYearRepository academicYearRepository;
    private final AcademicYearMapper academicYearMapper;

    public boolean existsByYearCode(String yearCode) {
        return academicYearRepository.existsByYearCode(yearCode);
    }

    public Long getActiveYearId() {
        return academicYearRepository.getByActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active year found"))
                .getId();
    }

    @Transactional
    public AcademicYearDto createAcademicYear(AcademicYearCreateRequest request) {
        Optional.of(request.isActive())
                .filter(Boolean::booleanValue)
                .ifPresent(v -> academicYearRepository.getByActiveTrue()
                        .stream()
                        .peek(y -> y.setActive(false))
                        .toList()
                        .forEach(academicYearRepository::save));

        var entity = new AcademicYearEntity();
        entity.setYearCode(request.yearCode());
        entity.setStartDate(request.startDate());
        entity.setEndDate(request.endDate());
        entity.setEnrollmentStart(request.enrollmentStart());
        entity.setEnrollmentEnd(request.enrollmentEnd());
        entity.setActive(request.isActive());

        var saved = academicYearRepository.save(entity);

        return academicYearMapper.toDto(saved);
    }
}
