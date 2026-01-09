package org.example.model.mapper;

import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.entity.CompletedCourseEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CompletedCourseMapper {
    CompletedCourseResponse toCompletedCourseDto(CompletedCourseEntity entity);
}
