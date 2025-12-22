package org.example.model.mapper;

import org.example.model.dto.CompletedCourseDto;
import org.example.model.entity.CompletedCourseEntity;
import org.springframework.stereotype.Component;

@Component
public class CompletedCourseMapper {

    public CompletedCourseDto toDto(CompletedCourseEntity entity) {
        var id = entity.getId().intValue();
        var studentId = entity.getStudent().getId().intValue();
        var courseId = entity.getCourse().getId().intValue();
        var grade = entity.getGrade();
        var completionDate = entity.getCompletionDate();
        var academicYearId = entity.getAcademicYear().getId().intValue();

        return new CompletedCourseDto(id, studentId, courseId, grade, completionDate, academicYearId);
    }
}