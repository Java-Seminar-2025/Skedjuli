package org.example.model.mapper;

import org.example.model.dto.UserDto;
import org.example.model.dto.UserInfo;
import org.example.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel="spring")
public interface UserMapper {

    @Mapping(source = "password", target = "passwordHash")
    UserDto toUserDto(UserEntity user);

    UserInfo toUserInfo(UserEntity user);
}