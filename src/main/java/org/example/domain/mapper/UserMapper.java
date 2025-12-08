package org.example.domain.mapper;

import org.example.domain.dto.DashboardResponse;
import org.example.domain.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static DashboardResponse toDto(UserEntity user) {
        return new DashboardResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}