package org.example.model.mapper;

import org.example.model.dto.CompletedCourseDto;
import org.example.model.entity.CompletedCourseEntity;
import org.springframework.stereotype.Component;

@Component
public class CompletedCourseMapper {

    public CompletedCourseDto toDto(CompletedCourseEntity entity) {
        var id = entity.getId();
        var studentId = entity.getStudent().getId();
        var courseId = entity.getCourse().getId();
        var grade = entity.getGrade();
        var completionDate = entity.getCompletionDate();
        var academicYearId = entity.getAcademicYear().getId();

        return new CompletedCourseDto(studentId, courseId, grade, completionDate, academicYearId);
    }
}