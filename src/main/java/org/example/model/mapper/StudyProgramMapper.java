package org.example.model.mapper;

import org.example.model.dto.StudyProgramDto;
import org.example.model.entity.StudyProgramEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyProgramMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    StudyProgramDto toStudyProgramDto(StudyProgramEntity studyProgram);
}
