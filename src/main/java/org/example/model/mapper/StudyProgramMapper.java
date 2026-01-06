package org.example.model.mapper;

import org.example.model.dto.StudyProgramDto;
import org.example.model.entity.StudyProgramEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudyProgramMapper {
    StudyProgramDto toStudyProgramDto(StudyProgramEntity studyProgram);
}
