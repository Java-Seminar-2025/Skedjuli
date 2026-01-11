package org.example.service.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.dto.request.create.CompletedCourseCreateRequest;
import org.example.model.dto.request.patch.CompletedCoursePatchRequest;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CompletedCourseEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.mapper.CompletedCourseMapper;
import org.example.repository.CompletedCourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompletedCourseDomainService {

    private final CompletedCourseRepository repository;
    private final CompletedCourseMapper mapper;
    private final EntityManager entityManager;

    @Transactional
    public CompletedCourseResponse createCompletedCourse(CompletedCourseCreateRequest request) {
        if (repository.existsByStudent_IdAndCourse_Id(request.studentId(), request.courseId())) {
            throw new IllegalStateException("Course already completed by student");
        }

        var entity = new CompletedCourseEntity();
        var studentRef = entityManager.getReference(StudentEntity.class, request.studentId());
        var courseRef = entityManager.getReference(CourseEntity.class, request.courseId());
        var yearRef = entityManager.getReference(AcademicYearEntity.class, request.academicYearId());

        entity.setStudent(studentRef);
        entity.setCourse(courseRef);
        entity.setGrade(request.grade());
        entity.setCompletionDate(request.completionDate());
        entity.setAcademicYear(yearRef);

        var saved = repository.save(entity);
        return mapper.toCompletedCourseDto(saved);
    }

    public Boolean hasCompletedCourse(Long studentId, Long courseId) {
        return repository.existsByStudent_IdAndCourse_Id(studentId, courseId);
    }

    public Set<Long> getCompletedCourseIdSet(Long studentId) {
        var set = repository.findByStudent_Id(studentId)
                .stream()
                .map(cc -> cc.getCourse().getId())
                .collect(Collectors.toCollection(HashSet::new));
        return Collections.unmodifiableSet(set);
    }

<<<<<<< HEAD
    @Transactional
    public CompletedCourseDto createCompletedCourse(Long studentId, Long courseId, int grade, LocalDate completionDate, Long academicYearId) {
        if (completedCourseRepository.existsByStudent_IdAndCourse_Id(studentId, courseId)) {
            throw new IllegalStateException("Course already completed by student");
        }
        var studentRef = entityManager.getReference(StudentEntity.class, studentId);
        var courseRef = entityManager.getReference(CourseEntity.class, courseId);
        var yearRef = entityManager.getReference(AcademicYearEntity.class, academicYearId);

        var entity = new CompletedCourseEntity();
        entity.setStudent(studentRef);
        entity.setCourse(courseRef);
        entity.setGrade(grade);
        entity.setCompletionDate(completionDate);
        entity.setAcademicYear(yearRef);

        var saved = completedCourseRepository.save(entity);

        return completedCourseMapper.toDto(saved);
    }

    public CompletedCourseDto getById(Long id) {
        var entity = completedCourseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Completed course not found with id: " + id));
        return completedCourseMapper.toDto(entity);
    }

    @Transactional
    public CompletedCourseDto update(Long id, CompletedCourseDto dto) {
        var entity = completedCourseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Completed course not found with id: " + id));

        entity.setGrade(dto.grade());
        entity.setCompletionDate(dto.completionDate());

        if (dto.academicYearId() != null) {
            var yearRef = entityManager.getReference(AcademicYearEntity.class, dto.academicYearId());
            entity.setAcademicYear(yearRef);
        }

        return completedCourseMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!completedCourseRepository.existsById(id)) {
            throw new EntityNotFoundException("Completed course not found with id: " + id);
        }
        completedCourseRepository.deleteById(id);
    }
=======
    public CompletedCourseResponse getById(Long id) {
        return mapper.toCompletedCourseDto(getCompletedCourseOrThrow(id));
    }

    @Transactional
    public CompletedCourseResponse patchCompletedCourse(Long id, CompletedCoursePatchRequest request) {
        var completedCourse = getCompletedCourseOrThrow(id);

        completedCourse.setGrade(request.grade());

        return mapper.toCompletedCourseDto(completedCourse);
    }

    @Transactional
    public void deleteCompletedCourse(Long id) {
        repository.delete(getCompletedCourseOrThrow(id));
    }

    private CompletedCourseEntity getCompletedCourseOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Completed course not found with id: " + id
                ));
        }
>>>>>>> origin/enrollment
}
