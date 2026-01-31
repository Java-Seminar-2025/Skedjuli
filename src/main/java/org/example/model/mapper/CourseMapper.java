package org.example.model.mapper;

import org.example.model.dto.response.CourseResponse;
import org.example.model.dto.unsorted.CourseInfo;
import org.example.model.dto.unsorted.CourseReadRequestDto;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.LecturerEntity;
import org.example.model.entity.StudyProgramEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "lecturerId", source = "lecturer", qualifiedByName = "mapLecturerId")
    @Mapping(target = "studyProgramId", source = "studyProgram", qualifiedByName = "mapStudyProgramId")
    @Mapping(target = "academicYearId", source = "academicYear", qualifiedByName = "mapAcademicYearId")
    @Mapping(target = "prerequisiteIds", expression = "java(java.util.Set.of())")
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

    CourseReadRequestDto toCourseReadRequestDto(CourseEntity course);
    CourseInfo toCourseInfo(CourseEntity course);
}
