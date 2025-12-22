package org.example.model.mapper;

import org.example.model.dto.UserDto;
import org.example.model.dto.UserInfo;

public class UserMapper {

    private UserMapper() {}

    public static UserInfo toUserInfo(UserDto user) {
        if (user == null) return null;
        return new UserInfo(
                user.id(),
                user.email(),
                user.firstName(),
                user.lastName(),
                user.role()
        );
    }
}
