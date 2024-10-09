package com.example.cnweb_nhom5.controller.client;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cnweb_nhom5.domain.User;
import com.example.cnweb_nhom5.service.EmailService;
import com.example.cnweb_nhom5.service.UserService;

@Controller
public class ForgotPasswordController {
    private final UserService userService;
    private final EmailService emailService;

    public ForgotPasswordController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Model model) {
        return "client/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email,
            Model model) {
        // Kiểm tra xem email có tồn tại không
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Email không tồn tại.");
            return "client/auth/forgot-password";
        }
        // Tạo mã reset ngẫu nhiên
        // String resetCode = UUID.randomUUID().toString();
        // Tạo mã reset ngẫu nhiên gồm 6 chữ số
        Random random = new Random();
        int resetCode = random.nextInt(900000) + 100000;

        // Lưu mã reset vào user (cần thêm field resetCode trong User)
        user.setResetCode(String.valueOf(resetCode)); // Chuyển đổi số thành chuỗi
        userService.save(user);

        // Gửi email
        emailService.sendResetPasswordEmail(user.getEmail(), String.valueOf(resetCode));

        model.addAttribute("email", email);
        model.addAttribute("message", "Reset code đã được gửi về email của bạn.");
        return "client/auth/reset-password";
    }
}
