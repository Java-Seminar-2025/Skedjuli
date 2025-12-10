package org.example.domain.mapper;

import org.example.domain.dto.UserDto;
import org.example.domain.dto.UserInfo;

public class UserMapper {

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
