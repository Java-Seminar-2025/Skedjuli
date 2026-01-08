package org.example.model.mapper;

import org.example.model.dto.CourseInfo;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.model.entity.*;
import org.example.service.domain.LecturerDomainService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseInfo toCourseInfo(CourseEntity course);

    @Mapping(target = "studyProgramId", source = "studyProgram")
    @Mapping(target = "academicYearId", source = "academicYear")
    @Mapping(target = "lecturerId", source = "lecturer")
    CoursePatchRequest toCoursePatchRequest(CourseEntity course);

    // ---------- Unique ID extractors ----------
    default Long mapStudyProgramId(StudyProgramEntity entity) {
        return entity == null ? null : entity.getId();
    }

    default Long mapAcademicYearId(AcademicYearEntity entity) {
        return entity == null ? null : entity.getId();
    }

    default Long mapLecturerId(LecturerEntity entity) {
        return entity == null ? null : entity.getId();
    }
}

