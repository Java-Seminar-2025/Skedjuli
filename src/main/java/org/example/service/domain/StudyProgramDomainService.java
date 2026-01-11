package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.model.dto.request.create.StudyProgramCreateRequest;
import org.example.model.dto.request.patch.StudyProgramPatchRequest;
import org.example.model.dto.response.StudyProgramResponse;
import org.example.model.entity.StudyProgramEntity;
import org.example.model.mapper.StudyProgramMapper;
import org.example.repository.StudyProgramRepository;
import org.example.service.validator.StudyProgramValidator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudyProgramDomainService {

    private final StudyProgramRepository repository;
    private final StudyProgramMapper mapper;

    @Transactional
    public StudyProgramResponse createStudyProgram(StudyProgramCreateRequest request) {
        StudyProgramValidator.validateCreate(request);

        var code = generateStudyProgramCode(request.name());

        var entity = new StudyProgramEntity(
                code,
                request.name(),
                request.durationYears()
        );

        entity.setDescription(request.description());
        entity.setTotalEcts(request.totalEcts());
        entity.setActive(request.active() != null ? request.active() : true);

        return mapper.toStudyProgramResponse( repository.save(entity));
    }

    public StudyProgramResponse getStudyProgramById(Long id) {
        return mapper.toStudyProgramResponse(getEntityOrThrow(id));
    }

    public List<StudyProgramResponse> getAllStudyPrograms() {
        return repository.findAll().stream()
                .map(mapper::toStudyProgramResponse)
                .toList();
    }

    @Transactional
    public StudyProgramResponse patchStudyProgram(Long id, StudyProgramPatchRequest request) {
        StudyProgramValidator.validatePatch(request);

        var entity = getEntityOrThrow(id);

        Optional.ofNullable(request.code()).ifPresent(entity::setCode);
        Optional.ofNullable(request.name()).ifPresent(entity::setName);
        Optional.ofNullable(request.description()).ifPresent(entity::setDescription);
        Optional.ofNullable(request.durationYears()).ifPresent(entity::setDurationYears);
        Optional.ofNullable(request.totalEcts()).ifPresent(entity::setTotalEcts);
        Optional.ofNullable(request.active()).ifPresent(entity::setActive);

        return mapper.toStudyProgramResponse(entity);
    }

    @Transactional
    public void deleteStudyProgram(Long id) {
        repository.delete(getEntityOrThrow(id));
    }

    private StudyProgramEntity getEntityOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StudyProgram not found with id: " + id));
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    private String generateStudyProgramCode(String name) {
        String initials = Arrays.stream(name.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase())
                .reduce("", String::concat);

        String code = initials;
        int counter = 1;
        while (repository.existsByCode(code)) {
            code = initials + counter;
            counter++;
        }

        return code;
    }
}
