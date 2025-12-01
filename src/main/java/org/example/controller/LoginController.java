package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.AuthRequest;
import org.example.model.AuthResponse;
import org.example.model.RegisterRequest;
import org.example.service.AuthService;
import org.example.service.UserService;
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
    private final UserService UserService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
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
        System.out.println("Email: " + req.getEmail());
        System.out.println("Role: " + req.getRole());
        System.out.println("First name: " + req.getFirstName());
        System.out.println("Last name: " + req.getLastName());
        try {
            AuthResponse response = authService.register(req);
            model.addAttribute("success", "User registered successfully! Email: " + response.getEmail());
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            AuthRequest authRequest = new AuthRequest();
            authRequest.setEmail(email);
            authRequest.setPassword(password);
            AuthResponse response = authService.login(authRequest);
            System.out.println("Login successful. AuthResponse Email: " + response.getEmail());
            System.out.println("Login successful. AuthResponse Token: " + response.getToken());
            model.addAttribute("success", "Login successful! Welcome back.");
            model.addAttribute("token", response.getToken());
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }
}