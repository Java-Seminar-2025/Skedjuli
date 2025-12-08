package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.domain.dto.AuthRequest;
import org.example.domain.dto.AuthResponse;
import org.example.domain.dto.RegisterRequest;
import org.example.service.business.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class LoginController {

    private final AuthService authService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest(
                "", "", "", "", "", "", "", "", "", "", null, null, null, null
        ));
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerRequest") RegisterRequest req, Model model) {
        // Debug logging
        System.out.println("Received registration:");
        System.out.println("Email: " + req.email());
        System.out.println("Role: " + req.role());
        System.out.println("First name: " + req.firstName());
        System.out.println("Last name: " + req.lastName());
        try {
            AuthResponse response = authService.register(req);
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
            AuthRequest authRequest = new AuthRequest(email, password);
            AuthResponse response = authService.login(authRequest);
            System.out.println("Login successful. AuthResponse Email: " + response.email());
            System.out.println("Login successful. AuthResponse Token: " + response.token());
            model.addAttribute("success", "Login successful! Welcome back.");
            model.addAttribute("token", response.token());
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }
}