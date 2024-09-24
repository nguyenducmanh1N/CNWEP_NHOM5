package com.example.cnweb_nhom5.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import com.example.cnweb_nhom5.domain.Category;
import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.service.CategoryService;
import com.example.cnweb_nhom5.service.ProductService;
import com.example.cnweb_nhom5.service.UploadService;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    private final CategoryService categoryService;
    public ProductController(
            UploadService uploadService,
            ProductService productService,
            CategoryService categoryService) {
        this.uploadService = uploadService;
        this.productService = productService;
        this.categoryService = categoryService;
    }
}
