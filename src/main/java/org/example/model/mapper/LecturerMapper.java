package org.example.model.mapper;

import org.example.model.dto.response.LecturerResponse;
import org.example.model.entity.LecturerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "title", source = "academicTitle")
    @Mapping(target = "office", source = "officeLocation")
    @Mapping(target = "phone", source = "phoneNumber")
    LecturerResponse toLecturerResponse(LecturerEntity entity);
}
