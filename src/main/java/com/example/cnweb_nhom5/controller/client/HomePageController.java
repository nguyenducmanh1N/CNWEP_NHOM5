package com.example.cnweb_nhom5.controller.client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.example.cnweb_nhom5.domain.User;
import com.example.cnweb_nhom5.domain.dto.RegisterDTO;

import com.example.cnweb_nhom5.service.UploadService;
import com.example.cnweb_nhom5.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomePageController {

    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    private final UploadService uploadService;
    
    public HomePageController(
            
            UserService userService,
            PasswordEncoder passwordEncoder,
            
            UploadService uploadService
           ) {
        
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        
        this.uploadService = uploadService;
        
    }

    @GetMapping("/")
    public String getHomePage(Model model
    ,@RequestParam("page") Optional<String> pageOptional
    ) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
            // TODO: handle exception
        }
        // List<Product> products = this.productService.fetchProducts();
        Pageable pageable = PageRequest.of(0, 12);
        
        
        model.addAttribute("currentPage", page);
       
       
       
        return "client/homepage/show";
    }
   


    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(Model model,
            @ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult,
             @RequestParam("flowershopFile") MultipartFile file) {

        // validate
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "client/auth/register";
        }

        User user = this.userService.registerDTOtoUser(registerDTO);
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(user.getPassword());

        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName("USER"));
        user.setAvatar(avatar);
        
        // save
        this.userService.handleSaveUser(user);
        return "redirect:/login";

    }
    
    @GetMapping("/login")
    public String getLoginPage(Model model) {

        return "client/auth/login";
    }

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {

        return "client/auth/403";
    }

    
}
