package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.AcademicYearCreateRequest;
import org.example.service.domain.AcademicYearDomainService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AcademicYearValidator {

    private final AcademicYearDomainService academicYearDomainService;

    public void validateCreate(AcademicYearCreateRequest request) {
        Optional.ofNullable(request)
                .orElseThrow(() -> new IllegalArgumentException("Request must not be null"));

        Optional.ofNullable(request.yearCode())
                .filter(v -> !v.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("yearCode must not be blank"));

        Optional.ofNullable(request.startDate())
                .orElseThrow(() -> new IllegalArgumentException("startDate must not be null"));

        Optional.ofNullable(request.endDate())
                .orElseThrow(() -> new IllegalArgumentException("endDate must not be null"));

        Optional.of(request.startDate().isBefore(request.endDate()))
                .filter(Boolean::booleanValue)
                .orElseThrow(() -> new IllegalArgumentException("startDate must be before endDate"));

        Optional.ofNullable(request.enrollmentStart())
                .ifPresent(es -> Optional.of(es.isAfter(request.startDate()) || es.isEqual(request.startDate()))
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException("enrollmentStart must be on/after startDate")));

        Optional.ofNullable(request.enrollmentEnd())
                .ifPresent(ee -> Optional.of(ee.isBefore(request.endDate()) || ee.isEqual(request.endDate()))
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException("enrollmentEnd must be on/before endDate")));

        Optional.ofNullable(request.enrollmentStart())
                .flatMap(es -> Optional.ofNullable(request.enrollmentEnd()).map(ee -> es.isBefore(ee) || es.isEqual(ee)))
                .ifPresent(ok -> Optional.of(ok)
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException("enrollmentStart must be before or equal to enrollmentEnd")));

        Optional.of(academicYearDomainService.existsByYearCode(request.yearCode()))
                .filter(exists -> !exists)
                .orElseThrow(() -> new IllegalStateException("Academic year with this yearCode already exists"));
    }
}