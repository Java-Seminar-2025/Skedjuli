package org.example.model.mapper;

import org.example.model.dto.AcademicYearDto;
import org.example.model.entity.AcademicYearEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    AcademicYearDto toDto(AcademicYearEntity academicYear);
}
