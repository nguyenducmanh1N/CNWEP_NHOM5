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

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("resetCode") String resetCode,
            @RequestParam("newPassword") String newPassword, Model model) {
        // Kiểm tra mã reset có hợp lệ không
        User user = userService.findByResetCode(resetCode);
        if (user == null) {
            model.addAttribute("error", "Reset code không hợp lệ.");
            return "client/auth/reset-password";
        }
        System.out.println("User found: " + user.getEmail());
        System.out.println("New password: " + newPassword);

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null); // Xóa mã reset sau khi dùng
        System.out.println("Encoded password: " + user.getPassword());

        userService.save(user);

        model.addAttribute("message", "Mật khẩu đã được cập nhật.");
        return "redirect:/login";
    }
   
}