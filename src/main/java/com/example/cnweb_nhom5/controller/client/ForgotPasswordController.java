package com.example.cnweb_nhom5.controller.client;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cnweb_nhom5.domain.User;

import com.example.cnweb_nhom5.service.UserService;

@Controller
public class ForgotPasswordController {
    private final UserService userService;
    

    public ForgotPasswordController(UserService userService) {
        this.userService = userService;
        
    }

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Model model) {
        return "client/auth/forgot-password";
    }

    
}
