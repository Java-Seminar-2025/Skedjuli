package org.example.model.mapper;

import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.entity.CompletedCourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompletedCourseMapper {
    @Mapping(target = "grade", source = "grade", defaultValue = "0")
    CompletedCourseResponse toCompletedCourseDto(CompletedCourseEntity entity);
}
