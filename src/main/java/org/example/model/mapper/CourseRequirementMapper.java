package org.example.model.mapper;

import org.example.model.dto.response.CourseRequirementResponse;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.CourseRequirementEntity;
import org.example.model.entity.StudyProgramEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface CourseRequirementMapper {

    @Mapping(target = "courseId", source = "course", qualifiedByName = "mapCourseId")
    @Mapping(target = "requiredCourseId", source = "requiredCourse", qualifiedByName = "mapCourseId")
    CourseRequirementResponse toCourseRequirementResponse(CourseRequirementEntity entity);

    @Named("mapCourseId")
    default Long mapCourseId(CourseEntity entity) {
        return entity == null ? null : entity.getId();
    }
}
