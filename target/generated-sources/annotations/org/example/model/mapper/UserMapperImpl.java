package org.example.model.mapper;

import javax.annotation.processing.Generated;
import org.example.model.dto.UserDto;
import org.example.model.dto.UserInfo;
import org.example.model.entity.UserEntity;
import org.example.model.enums.Role;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-27T14:41:36+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.8 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        String passwordHash = null;
        Long id = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        Role role = null;

        passwordHash = user.getPassword();
        id = user.getId();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        role = user.getRole();

        UserDto userDto = new UserDto( id, email, passwordHash, firstName, lastName, role );

        return userDto;
    }

    @Override
    public UserInfo toUserInfo(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        Role role = null;

        id = user.getId();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        role = user.getRole();

        UserInfo userInfo = new UserInfo( id, email, firstName, lastName, role );

        return userInfo;
    }
}
