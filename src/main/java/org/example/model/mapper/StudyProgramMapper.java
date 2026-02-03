package org.example.model.mapper;


import org.example.model.dto.response.StudyProgramResponse;
import org.example.model.entity.StudyProgramEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudyProgramMapper {
    StudyProgramResponse toStudyProgramResponse(StudyProgramEntity entity);
}
