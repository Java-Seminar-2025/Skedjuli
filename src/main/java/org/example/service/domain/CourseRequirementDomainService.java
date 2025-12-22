package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseRequirementDto;
import org.example.model.mapper.CourseRequirementMapper;
import org.example.repository.CourseRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseRequirementDomainService {

    private final CourseRequirementRepository courseRequirementRepository;

    public List<CourseRequirementDto> findByCourseId(Long courseId) {
        if (courseId == null) return List.of();
        var rows = courseRequirementRepository.findByCourse_Id(courseId);
        return CourseRequirementMapper.toDtoList(rows);
    }

    public List<CourseRequirementDto> findByCourseIds(Collection<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) return List.of();
        var rows = courseRequirementRepository.findByCourse_IdIn(courseIds);
        return CourseRequirementMapper.toDtoList(rows);
    }

    public List<CourseRequirementDto> findAll() {
        var rows = courseRequirementRepository.findAll();
        return CourseRequirementMapper.toDtoList(rows);
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
}
