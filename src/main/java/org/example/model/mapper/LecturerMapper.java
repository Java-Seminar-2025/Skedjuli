package org.example.model.mapper;

import org.example.model.dto.response.LecturerResponse;
import org.example.model.entity.LecturerEntity;
import org.mapstruct.Mapper;

@Mapper
public interface LecturerMapper {
    LecturerResponse toLecturerResponse(LecturerEntity entity);
}
