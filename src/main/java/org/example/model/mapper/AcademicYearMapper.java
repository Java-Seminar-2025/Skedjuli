package org.example.model.mapper;

import org.example.model.dto.AcademicYearDto;
import org.example.model.entity.AcademicYearEntity;
import org.springframework.stereotype.Component;

@Component
public class AcademicYearMapper {

    public AcademicYearDto toDto(AcademicYearEntity entity) {
        var id = entity.getId().intValue();
        var yearCode = entity.getYearCode();
        var startDate = entity.getStartDate();
        var endDate = entity.getEndDate();
        var enrollmentStart = entity.getEnrollmentStart();
        var enrollmentEnd = entity.getEnrollmentEnd();
        var isActive = Boolean.TRUE.equals(entity.getActive());

        return new AcademicYearDto(id, yearCode, startDate, endDate, enrollmentStart, enrollmentEnd, isActive);
    }
}
