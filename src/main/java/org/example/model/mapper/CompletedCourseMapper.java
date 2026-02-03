package org.example.model.mapper;

import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.entity.CompletedCourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompletedCourseMapper {
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "academicYearId", source = "academicYear.id")
    CompletedCourseResponse toCompletedCourseDto(CompletedCourseEntity entity);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "academicYearId", source = "academicYear.id")
    CompletedCourseResponse toResponse(CompletedCourseEntity entity);
}
