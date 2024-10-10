package com.example.cnweb_nhom5.service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.cnweb_nhom5.domain.Category;
import com.example.cnweb_nhom5.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Page<Category> fetchCategories(Pageable page) {
        return this.categoryRepository.findAll(page);
    }
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> fetchAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> fetchCategoryById(long id) {
        return categoryRepository.findById(id);
    }

    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    public Category getCategorybyName(String name){
        return this.categoryRepository.findByName(name);
    }
    public Category findById(Long categoryId) {
        // Tìm kiếm Category bằng categoryId từ database
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
    }
}
