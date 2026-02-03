package org.example.service.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.response.UserResponse;
import org.example.service.domain.UserDomainService;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@Service
public class DashboardService {

    private final UserDomainService userDomainService;

    public UserResponse getDashboardResponse(String email) {
        return userDomainService.getUserResponseByEmail(email);
    }
}
