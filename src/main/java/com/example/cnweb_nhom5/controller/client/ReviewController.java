package com.example.cnweb_nhom5.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.domain.Review;
import com.example.cnweb_nhom5.domain.User;
import com.example.cnweb_nhom5.service.ProductService;
import com.example.cnweb_nhom5.service.ReviewService;
import com.example.cnweb_nhom5.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.time.LocalDateTime;

@Controller
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;

    
    public ReviewController(ReviewService reviewService, ProductService productService, UserService userService)
    {
        this.reviewService = reviewService;
        this.productService = productService;
        this.userService = userService;
    }
    @GetMapping("/product/review/{productId}")
    public String getCreateReviewPage(@PathVariable Long productId,Model model) {
 
        model.addAttribute("productId", productId);
        model.addAttribute("newReview", new Review());
        return "client/product/review";
    }


    @PostMapping("/client/product/review")
    public String handleCreateReview(
            @RequestParam("productId") Long productId,
            @ModelAttribute("newReview") @Valid Review rv,
            BindingResult newProductBindingResult,
            Model model) {
        if (newProductBindingResult.hasErrors()) {
            return "client/product/review";
        }

        Optional<Product> productOptional = this.productService.fetchProductById(productId);
        if (productOptional.isEmpty()) {
            model.addAttribute("error", "Product not found");
            return "client/product/review";
        }

        rv.setProduct(productOptional.get());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Tìm User bằng email thay vì username
        com.example.cnweb_nhom5.domain.User currentUser = this.userService.findByEmail(userDetails.getUsername());

        rv.setUser(currentUser);
        this.reviewService.createReview(rv);

        return "redirect:/product/" + productId;
    }

}
