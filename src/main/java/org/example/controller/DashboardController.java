package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/dashboard")
    public User getDashboardData(Principal principal) {
        String email = principal.getName();
        return java.util.Optional.ofNullable(userService.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
