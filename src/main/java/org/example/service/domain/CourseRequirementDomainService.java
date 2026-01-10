package org.example.service.domain;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseRequirementCreateRequest;
import org.example.model.dto.request.patch.CourseRequirementPatchRequest;
import org.example.model.dto.response.CourseRequirementResponse;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.CourseRequirementEntity;
import org.example.model.mapper.CourseRequirementMapper;
import org.example.repository.CourseRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseRequirementDomainService {

    private final CourseRequirementRepository repository;
    private final CourseRequirementMapper mapper;
    private final EntityManager entityManager;

    @Transactional
    public CourseRequirementResponse createCourseRequirement(CourseRequirementCreateRequest request) {
        var courseRequirement = new CourseRequirementEntity();
        var course = entityManager.getReference(CourseEntity.class, request.courseId());
        var requiredCourse = entityManager.getReference(CourseEntity.class, request.requiredCourseId());

        courseRequirement.setCourse(course);
        courseRequirement.setRequiredCourse(requiredCourse);

        var saved = repository.save(courseRequirement);
        return mapper.toCourseRequirementResponse(saved);
    }

    public List<CourseRequirementResponse> findByCourseId(Long courseId) {
        if (courseId == null) return List.of();
        var rows = repository.findByCourse_Id(courseId);
        return rows.stream().map(mapper::toCourseRequirementResponse).toList();
    }

    public List<CourseRequirementResponse> findByCourseIds(Collection<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) return List.of();
        var rows = repository.findByCourse_IdIn(courseIds);
        return rows.stream().map(mapper::toCourseRequirementResponse).toList();
    }

    public List<CourseRequirementResponse> findAll() {
        var rows = repository.findAll();
        return rows.stream().map(mapper::toCourseRequirementResponse).toList();
    }

    public Map<Long, List<Long>> getDirectPrereqsMap(Collection<Long> courseIds) {
        return findByCourseIds(courseIds).stream()
                .collect(Collectors.groupingBy(
                        CourseRequirementResponse::courseId,
                        Collectors.mapping(CourseRequirementResponse::requiredCourseId, Collectors.toList())
                ));
    }

    public List<Long> getDirectPrerequisiteIds(Long courseId) {
        return findByCourseId(courseId).stream()
                .map(CourseRequirementResponse::requiredCourseId)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseRequirementResponse patchCourseRequirement(Long id, CourseRequirementPatchRequest request) {
        var courseRequirement = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course requirement not found"));
        var requiredCourse = entityManager.getReference(CourseEntity.class, request.requiredCourseId());

        courseRequirement.setRequiredCourse(requiredCourse);

        return mapper.toCourseRequirementResponse(courseRequirement);
    }


    @Transactional
    public void deleteCourseRequirement(Long id) {
        var requirement = repository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Course requirement not found"));
        repository.delete(requirement);
    }

    @Transactional
    public void deleteRequirementsForCourse(Long courseId) {
        repository.findByCourse_Id(courseId)
                .forEach(repository::delete);
    }
}
