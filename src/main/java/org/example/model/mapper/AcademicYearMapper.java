package org.example.model.mapper;

import org.example.model.dto.response.AcademicYearResponse;
import org.example.model.entity.AcademicYearEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    @Mapping(target = "isActive", source="active")
    AcademicYearResponse toAcademicYearResponse(AcademicYearEntity entity);
}
