package org.example.model.mapper;

import org.example.model.dto.response.AcademicYearResponse;
import org.example.model.entity.AcademicYearEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    AcademicYearResponse toAcademicYearResponse(AcademicYearEntity entity);
}
