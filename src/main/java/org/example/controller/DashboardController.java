package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class DashboardController {

    private final UserService userService;

    @GetMapping("/api/dashboard")
    public User getDashboardData(Principal principal) {
        String email = principal.getName();
        return java.util.Optional.ofNullable(userService.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
