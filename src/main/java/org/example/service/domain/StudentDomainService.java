package org.example.service.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.model.dto.request.create.StudentCreateRequest;
import org.example.model.dto.request.patch.StudentPatchRequest;
import org.example.model.dto.response.StudentResponse;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudyProgramEntity;
import org.example.model.entity.UserEntity;
import org.example.model.mapper.StudentMapper;
import org.example.repository.StudentRepository;

import org.example.repository.specification.StudentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentDomainService {

    private final StudentRepository repository;
    private final StudentMapper mapper;
    private final EntityManager entityManager;

    public StudentResponse createStudent(StudentCreateRequest request) {
        var student = new StudentEntity();

        student.setUser(entityManager.getReference(UserEntity.class, request.userId()));
        student.setStudyProgram(entityManager.getReference(StudyProgramEntity.class, request.studyProgramId()));
        student.setEnrollmentYear(request.enrollmentYear());
        student.setCurrentYear(request.currentYear());
        student.setIsActive(true);

        var saved = repository.save(student);

        return mapper.toStudentResponse(saved);
    }

    public Long getStudyProgramIdByStudentId(Long studentId) {
        var student = getStudentOrThrow(studentId);

        return student.getStudyProgram().getId();
    }

    public Integer getCurrentYearById(Long studentId) {
        var student = getStudentOrThrow(studentId);

        return student.getCurrentYear();
    }

    @Transactional
    public StudentResponse patchStudent(Long studentId, StudentPatchRequest request) {
        var student = getStudentOrThrow(studentId);

        if (request.currentYear() != null) {
            student.setCurrentYear(request.currentYear());
        }

        if (request.isActive() != null) {
            student.setIsActive(request.isActive());
        }

        return mapper.toStudentResponse(student);
    }

    public StudentResponse getStudent(Long studentId) {
        var student = getStudentOrThrow(studentId);

        return mapper.toStudentResponse(student);
    }

    public void deleteStudent(Long studentId) {
        var student = getStudentOrThrow(studentId);

        repository.delete(student);
    }

    public Page<StudentResponse> getStudents(int page, int size, String sortBy, String sortOrder, Long studyProgramId, Integer enrollmentYear, Integer currentYear, Double totalEctsEarned, Boolean isActive) {
        var sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        var students = repository.findAll(
                StudentSpecification.filter(studyProgramId, enrollmentYear, currentYear, totalEctsEarned, isActive), pageable
        );

        return students.map(mapper::toStudentResponse);
    }

    public Long getStudentIdByUserId(Long userId) {
        return repository.findByUser_Id(userId)
                .orElseThrow(()->new EntityNotFoundException("Student not found for user id: " + userId))
                .getId();
    }

    private StudentEntity getStudentOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }
}