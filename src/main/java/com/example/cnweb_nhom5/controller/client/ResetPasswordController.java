package com.example.cnweb_nhom5.controller.client;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cnweb_nhom5.domain.User;
import com.example.cnweb_nhom5.service.UserService;

@Controller
public class ResetPasswordController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/reset-password")
    public String getResetPasswordPage(Model model) {
        return "client/auth/reset-password";
    }

   
}