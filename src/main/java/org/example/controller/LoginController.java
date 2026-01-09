package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.dto.AuthRequest;
import org.example.model.dto.request.create.UserCreateRequest;
import org.example.service.business.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@Validated
public class LoginController {

    private final AuthService authService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        var req = new UserCreateRequest(
                "",
                "",
                "",
                "student",
                "",
                "",
                "",
                "",
                "",
                "",
                null,
                null,
                null,
                null
        );
        model.addAttribute("registerRequest", req);
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") UserCreateRequest req, Model model) {
        try {
            var response = authService.register(req);
            model.addAttribute("success", "User registered successfully! Email: " + response.email());
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            var authRequest = new AuthRequest(email, password);
            var response = authService.login(authRequest);
            model.addAttribute("success", "Login successful! Welcome back.");
            model.addAttribute("token", response.token());
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }
}
