package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Dobrodošli u Skedjuli!");
        model.addAttribute("pageTitle", "Skedjuli App");
        return "index";
    }
}