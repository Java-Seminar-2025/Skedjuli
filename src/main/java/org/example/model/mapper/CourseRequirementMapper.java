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

    public static CourseRequirementDto toDto(CourseRequirementEntity entity) {
        if (entity == null) return null;

        Long courseId = Optional.of(entity.getCourse())
                .map(CourseEntity::getId)
                .orElse(null);

        Long reqId = Optional.of(entity.getRequiredCourse())
                .map(CourseEntity::getId)
                .orElse(null);

        return new CourseRequirementDto(entity.getId(), courseId, reqId, entity.getCreatedAt());
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
