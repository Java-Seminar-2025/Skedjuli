package org.example.controller;

import org.example.model.AuthRequest;
import org.example.model.AuthResponse;
import org.example.model.User;
import org.example.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(org.example.model.RegisterRequest req, Model model) {
        try {
            authService.register(req);   // sending reg req
            model.addAttribute("success", "Registration successful! You can now login.");
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }


    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        try {
            AuthRequest authRequest = new AuthRequest();
            authRequest.setEmail(email);
            authRequest.setPassword(password);

            AuthResponse response = authService.login(authRequest);


            model.addAttribute("success", "Login successful! Welcome back.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }
}