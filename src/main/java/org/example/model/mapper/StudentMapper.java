package org.example.model.mapper;

import org.example.model.dto.response.StudentResponse;
import org.example.model.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StudentMapper{
    @Mapping(target = "userId", source ="user.id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "studyProgramId", source = "studyProgram.id")
    StudentResponse toStudentResponse(StudentEntity entity);
}
