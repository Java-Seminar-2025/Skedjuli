package org.example.model.mapper;

import org.example.model.dto.CourseRequirementDto;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.CourseRequirementEntity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CourseRequirementMapper {

    private CourseRequirementMapper() { }

    public static CourseRequirementDto toDto(CourseRequirementEntity e) {
        if (e == null) return null;

        Long courseId = Optional.of(e.getCourse())
                .map(CourseEntity::getId)
                .orElse(null);

        Long reqId = Optional.of(e.getRequiredCourse())
                .map(CourseEntity::getId)
                .orElse(null);

        return new CourseRequirementDto(e.getId(), courseId, reqId, e.getCreatedAt());
    }

    public static List<CourseRequirementDto> toDtoList(Collection<CourseRequirementEntity> entities) {
        return Optional.ofNullable(entities)
                .map(list -> list.stream()
                        .filter(Objects::nonNull)
                        .map(CourseRequirementMapper::toDto)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
}
