package com.example.cnweb_nhom5.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String toEmail,
     String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("manhn0194@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Reset Password");
        message.setText("Your reset code is: " + resetCode);
        mailSender.send(message);

        System.out.println("successfully ....");
    }
}
