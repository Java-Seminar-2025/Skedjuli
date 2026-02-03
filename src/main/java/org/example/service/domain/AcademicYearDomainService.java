package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.model.dto.request.create.AcademicYearCreateRequest;
import org.example.model.dto.request.patch.AcademicYearPatchRequest;
import org.example.model.dto.response.AcademicYearResponse;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.mapper.AcademicYearMapper;
import org.example.repository.AcademicYearRepository;
import org.example.service.validator.AcademicYearValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AcademicYearDomainService {

    private final AcademicYearRepository repository;
    private final AcademicYearMapper mapper;

    @Transactional
    public AcademicYearResponse createAcademicYear(AcademicYearCreateRequest request) {
        AcademicYearValidator.validateCreate(request);

        if (Boolean.TRUE.equals(request.isActive())) {
            repository.getByActiveTrue().ifPresent(year -> year.setActive(false));
        }

        var entity = new AcademicYearEntity();
        entity.setYearCode(generateYearCode(request.startDate(), request.endDate()));
        entity.setStartDate(request.startDate());
        entity.setEndDate(request.endDate());
        entity.setEnrollmentStart(request.enrollmentStart());
        entity.setEnrollmentEnd(request.enrollmentEnd());
        entity.setActive(request.isActive());

        var saved = repository.save(entity);
        return mapper.toAcademicYearResponse(saved);
    }

    public AcademicYearResponse getActiveAcademicYear() {
        var activeYear = repository.getByActiveTrue()
                .orElseThrow(() -> new EntityNotFoundException("No active academic year found"));
        return mapper.toAcademicYearResponse(activeYear);
    }

    public AcademicYearResponse getAcademicYearById(Long id) {
        return mapper.toAcademicYearResponse(getAcademicYearOrThrow(id));
    }

    public List<AcademicYearResponse> getAllAcademicYears() {
        return repository.findAll().stream()
                .map(mapper::toAcademicYearResponse)
                .toList();
    }

    @Transactional
    public AcademicYearResponse patchAcademicYear(Long id, AcademicYearPatchRequest request) {
        AcademicYearValidator.validatePatch(request);

        var academicYear = getAcademicYearOrThrow(id);

        Optional.ofNullable(request.startDate()).ifPresent(academicYear::setStartDate);
        Optional.ofNullable(request.endDate()).ifPresent(academicYear::setEndDate);
        Optional.ofNullable(request.enrollmentStart()).ifPresent(academicYear::setEnrollmentStart);
        Optional.ofNullable(request.enrollmentEnd()).ifPresent(academicYear::setEnrollmentEnd);

        Optional.ofNullable(request.startDate())
                .or(() -> Optional.ofNullable(request.endDate()))
                .ifPresent(_ignored -> academicYear.setYearCode(generateYearCode(
                        academicYear.getStartDate(), academicYear.getEndDate()
                )));

        Optional.ofNullable(request.isActive()).ifPresent(isActive -> {
            handleActiveYear(id, isActive);
            academicYear.setActive(isActive);
        });

        return mapper.toAcademicYearResponse(academicYear);
    }

    @Transactional
    public void deleteAcademicYear(Long id) {
        repository.delete(getAcademicYearOrThrow(id));
    }

    private AcademicYearEntity getAcademicYearOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Academic year not found with id: " + id)
                );
    }

    private void handleActiveYear(Long id, Boolean isActive) {
        if (Boolean.TRUE.equals(isActive)) {
            repository.getByActiveTrue()
                    .filter(y -> !y.getId().equals(id))
                    .ifPresent(y -> y.setActive(false));
        }
    }

    private String generateYearCode(LocalDate startDate, LocalDate endDate) {
        return startDate.getYear() + "/" + endDate.getYear();
    }
}
