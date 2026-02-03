package org.example.service.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.model.dto.request.create.LecturerCreateRequest;
import org.example.model.dto.request.patch.LecturerPatchRequest;
import org.example.model.dto.response.LecturerResponse;
import org.example.model.entity.LecturerEntity;
import org.example.model.entity.UserEntity;
import org.example.model.mapper.LecturerMapper;
import org.example.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LecturerDomainService {

    private final LecturerRepository repository;
    private final LecturerMapper mapper;
    private final EntityManager entityManager;

    @Transactional
    public LecturerResponse createLecturer(LecturerCreateRequest request) {
        var lecturer = new LecturerEntity();

        lecturer.setUser(entityManager.getReference(UserEntity.class, request.userId()));
        lecturer.setDepartment(request.department());
        lecturer.setAcademicTitle(request.title());
        lecturer.setOfficeLocation(request.office());
        lecturer.setPhoneNumber(request.phone());
        lecturer.setIsActive(true);

        var saved = repository.save(lecturer);
        return mapper.toLecturerResponse(saved);
    }

    public LecturerResponse getLecturerById(Long id) {
        return mapper.toLecturerResponse(getEntityOrThrow(id));
    }

    public List<LecturerResponse> getAllLecturers() {
        return repository.findAll().stream()
                .map(mapper::toLecturerResponse)
                .toList();
    }

    @Transactional
    public LecturerResponse patchLecturer(Long id, LecturerPatchRequest request) {
        var lecturer = getEntityOrThrow(id);

        Optional.ofNullable(request.department()).ifPresent(lecturer::setDepartment);
        Optional.ofNullable(request.title()).ifPresent(lecturer::setAcademicTitle);
        Optional.ofNullable(request.office()).ifPresent(lecturer::setOfficeLocation);
        Optional.ofNullable(request.phone()).ifPresent(lecturer::setPhoneNumber);
        Optional.ofNullable(request.isActive()).ifPresent(lecturer::setIsActive);

        return mapper.toLecturerResponse(lecturer);
    }

    @Transactional
    public void deleteLecturer(Long id) {
        repository.delete(getEntityOrThrow(id));
    }

    public long getLecturerIdByUserId(Long userId) {
        return repository.findByUser_Id(userId)
                .orElseThrow(()->new EntityNotFoundException("Lecturer not found for user id: " + userId))
                .getId();
    }

    private LecturerEntity getEntityOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lecturer not found with id: " + id));
    }
}
