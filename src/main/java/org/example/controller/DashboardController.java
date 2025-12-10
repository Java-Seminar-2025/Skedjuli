package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.domain.dto.UserInfo;
import org.example.service.business.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/api/dashboard")
    public UserInfo getDashboardData(Principal principal) {
        String email = principal.getName();
        return dashboardService.getDashboardResponse(email);
    }
}
