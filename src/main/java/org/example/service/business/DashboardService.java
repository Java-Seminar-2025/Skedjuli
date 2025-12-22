package org.example.service.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.UserDto;
import org.example.model.dto.UserInfo;
import org.example.model.mapper.UserMapper;
import org.example.service.domain.UserDomainService;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@Service
public class DashboardService {

    private final UserDomainService userDomainService;

    public UserInfo getDashboardResponse(String email) {
        UserDto userDto = userDomainService.getUserDtoByEmail(email);
        return UserMapper.toUserInfo(userDto);
    }
}
