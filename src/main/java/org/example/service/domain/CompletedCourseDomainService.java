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
}
