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
    
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        List<Category> categories = categoryService.fetchAllCategories(); 
        
        // Thêm danh sách category vào model để truyền sang view
        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", new Product()); 
        return "admin/product/create"; 
    }
    
    @PostMapping("/admin/product/create")
    public String handleCreateProduct(
            @ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("hoidanitFile") MultipartFile file) {
        String image = this.uploadService.handleSaveUploadFile(file, "product");
        pr.setImage(image); 
        Category category = this.categoryService.findById(categoryId);
        pr.setCategory(category);
        this.productService.createProduct(pr);
        return "redirect:/admin/product"; 
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Optional<Product> currentProduct = this.productService.fetchProductById(id); 
        List<Category> categories = categoryService.fetchAllCategories(); 
        model.addAttribute("newProduct", currentProduct.get()); 
        model.addAttribute("categories", categories); 
        return "admin/product/update"; 
    }
    
    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult,
            @RequestParam("categoryId") Long categoryId, 
            @RequestParam("hoidanitFile") MultipartFile file) {

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update"; 
        }

        Product currentProduct = this.productService.fetchProductById(pr.getId()).get(); 
        if (currentProduct != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }

            currentProduct.setName(pr.getName());
            currentProduct.setPrice(pr.getPrice());
            currentProduct.setQuantity(pr.getQuantity());
            currentProduct.setDetailDesc(pr.getDetailDesc());
            currentProduct.setShortDesc(pr.getShortDesc());
            currentProduct.setFactory(pr.getFactory());
            currentProduct.setTarget(pr.getTarget());
            Category category = this.categoryService.findById(categoryId);
            currentProduct.setCategory(category);
    
            this.productService.createProduct(currentProduct); 
        }

        return "redirect:/admin/product"; 
    }
    
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id); 
        model.addAttribute("newProduct", new Product()); 
        return "admin/product/delete"; 
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product pr) {
        this.productService.deleteProduct(pr.getId());
        return "redirect:/admin/product"; 
    }
}
