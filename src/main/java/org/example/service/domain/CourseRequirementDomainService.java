package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseRequirementDto;
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

    @Transactional
    public CourseRequirementDto createRequirement(CourseEntity course, CourseEntity requiredCourse) {
        var entity = new CourseRequirementEntity();

        entity.setCourse(course);
        entity.setRequiredCourse(requiredCourse);

        var saved = repository.save(entity);
        return CourseRequirementMapper.toDto(saved);
    }

    public List<CourseRequirementDto> findByCourseId(Long courseId) {
        if (courseId == null) return List.of();
        var rows = repository.findByCourse_Id(courseId);
        return CourseRequirementMapper.toDtoList(rows);
    }

    public List<CourseRequirementDto> findByCourseIds(Collection<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) return List.of();
        var rows = repository.findByCourse_IdIn(courseIds);
        return CourseRequirementMapper.toDtoList(rows);
    }

    public List<CourseRequirementDto> findAll() {
        return CourseRequirementMapper.toDtoList(repository.findAll());
    }

    public Map<Long, List<Long>> getDirectPrereqsMap(Collection<Long> courseIds) {
        return findByCourseIds(courseIds).stream()
                .collect(Collectors.groupingBy(
                        CourseRequirementDto::courseId,
                        Collectors.mapping(CourseRequirementDto::requiredCourseId, Collectors.toList())
                ));
    }

    public List<Long> getDirectPrerequisiteIds(Long courseId) {
        return findByCourseId(courseId).stream()
                .map(CourseRequirementDto::requiredCourseId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void patchRequirements(Long courseId, Set<Long> newRequiredCourseIds, Map<Long, CourseEntity> courseMap) {
        repository.findByCourse_Id(courseId).stream()
                .filter(req -> !newRequiredCourseIds.contains(req.getRequiredCourse().getId()))
                .forEach(repository::delete);

        newRequiredCourseIds.stream()
                .filter(reqId -> repository.findByCourse_IdAndRequiredCourse_Id(courseId, reqId).isEmpty())
                .map(reqId -> new CourseRequirementEntity(
                        courseMap.get(courseId),
                        courseMap.get(reqId)
                ))
                .forEach(repository::save);
    }


    @Transactional
    public void deleteRequirement(Long courseId, Long requiredCourseId) {
        var requirement = repository.findByCourse_IdAndRequiredCourse_Id(courseId, requiredCourseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course requirement not found for courseId=" + courseId + " and requiredCourseId=" + requiredCourseId
                ));
        repository.delete(requirement);
    }

    @Transactional
    public void deleteRequirementsForCourse(Long courseId) {
        repository.findByCourse_Id(courseId)
                .forEach(repository::delete);
    }
}
