package org.example.model.mapper;

import org.example.model.dto.response.StudentResponse;
import org.example.model.entity.StudentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper{
    StudentResponse toStudentResponse(StudentEntity entity);
}
