package com.example.cnweb_nhom5.controller.admin;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.cnweb_nhom5.domain.Category;
import com.example.cnweb_nhom5.service.CategoryService;

import jakarta.validation.Valid;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Phương thức để lấy danh sách danh mục
    @GetMapping("/admin/category")
    public String getCategoryList(Model model) {
        List<Category> categories = categoryService.fetchAllCategories();
        model.addAttribute("categories", categories);
        return "admin/category/show";
    }

    // Phương thức để hiển thị trang tạo danh mục mới
    @GetMapping("/admin/category/create")
    public String getCreateCategoryPage(Model model) {
        model.addAttribute("newCategory", new Category());
        return "admin/category/create";
    }

    // Phương thức để xử lý tạo danh mục mới
    @PostMapping("/admin/category/create")
    public String handleCreateCategory(
            @ModelAttribute("newCategory") @Valid Category category,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/create";
        }

        categoryService.createCategory(category);
        return "redirect:/admin/category";
    }

    // Phương thức để hiển thị trang cập nhật danh mục
    @GetMapping("/admin/category/update/{id}")
    public String getUpdateCategoryPage(Model model, @PathVariable long id) {
        Optional<Category> category = categoryService.fetchCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("newCategory", category.get());
            return "admin/category/update";
        }
        return "redirect:/admin/category";
    }

    // Phương thức để xử lý cập nhật danh mục
    @PostMapping("/admin/category/update")
    public String handleUpdateCategory(
            @ModelAttribute("newCategory") @Valid Category category,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/update";
        }

        categoryService.createCategory(category);
        return "redirect:/admin/category";
    }

    // Phương thức để xóa danh mục
    @GetMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }
}
