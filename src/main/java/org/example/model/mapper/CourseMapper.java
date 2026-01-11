package org.example.model.mapper;

import org.example.model.dto.response.CourseResponse;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.LecturerEntity;
import org.example.model.entity.StudyProgramEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface CourseMapper {
<<<<<<< HEAD
    CourseReadRequestDto toCourseReadRequestDto(CourseEntity course);
    CourseInfo toCourseInfo(CourseEntity course);
=======

    @Mapping(target = "lecturerId", source = "lecturer", qualifiedByName = "mapLecturerId")
    @Mapping(target = "studyProgramId", source = "studyProgram", qualifiedByName = "mapStudyProgramId")
    @Mapping(target = "academicYearId", source = "academicYear", qualifiedByName = "mapAcademicYearId")
    @Mapping(target = "prerequisiteIds", expression = "java(mapPrerequisites(entity))")
    CourseResponse toCourseResponse(CourseEntity entity);

    @Named("mapStudyProgramId")
    default Long mapStudyProgramId(StudyProgramEntity entity) {
        return entity == null ? null : entity.getId();
    }

    @Named("mapAcademicYearId")
    default Long mapAcademicYearId(AcademicYearEntity entity) {
        return entity == null ? null : entity.getId();
    }

    @Named("mapLecturerId")
    default Long mapLecturerId(LecturerEntity entity) {
        return entity == null ? null : entity.getId();
    }

    default Set<Long> mapPrerequisites(CourseEntity entity) {
        if (entity == null || entity.getPrerequisites() == null) return Set.of();
        return entity.getPrerequisites().stream()
                .map(CourseEntity::getId)
                .collect(Collectors.toSet());
    }
>>>>>>> origin/enrollment
}
