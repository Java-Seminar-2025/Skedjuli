package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.dto.response.UserResponse;
import org.example.service.business.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/api/dashboard")
    public UserResponse getDashboardData(@RequestParam String email) {
        return dashboardService.getDashboardResponse(email);
    }
}
