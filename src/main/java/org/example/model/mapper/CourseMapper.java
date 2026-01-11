package org.example.model.mapper;

import org.example.model.dto.CourseInfo;
import org.example.model.dto.CourseReadRequestDto;
import org.example.model.entity.CourseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseReadRequestDto toCourseReadRequestDto(CourseEntity course);
    CourseInfo toCourseInfo(CourseEntity course);
}
