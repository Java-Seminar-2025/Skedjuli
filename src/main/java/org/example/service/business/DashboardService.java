package org.example.service.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.dto.DashboardResponse;
import org.example.service.domain.UserDomainService;
import org.springframework.stereotype.Service;
import org.example.domain.mapper.UserMapper;

@Getter
@AllArgsConstructor
@Service
public class DashboardService {
    private final UserDomainService userDomainService;

    public DashboardResponse getDashboardResponse(String email) {
        return UserMapper.toDto(userDomainService.getByEmail(email));
    }
}
