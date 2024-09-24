package com.example.cnweb_nhom5.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.domain.dto.ProductCriteriaDTO;
import com.example.cnweb_nhom5.repository.ProductRepository;
import com.example.cnweb_nhom5.service.specification.ProductSpecs;

@Service
public class ProductService {

    // Repository để tương tác với cơ sở dữ liệu cho sản phẩm
    private final ProductRepository productRepository;

    // Constructor để khởi tạo ProductRepository
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Tạo một sản phẩm mới và lưu vào cơ sở dữ liệu
    public Product createProduct(Product pr) {
        return this.productRepository.save(pr);
    }

    // Lấy danh sách sản phẩm với phân trang
    public Page<Product> fetchProducts(Pageable page) {
        return this.productRepository.findAll(page);
    }

    // Lấy danh sách sản phẩm theo tiêu chí tìm kiếm với phân trang
    public Page<Product> fetchProductsWithSpec(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        // Kiểm tra xem có tiêu chí nào không
        if (productCriteriaDTO.getTarget() == null
                && productCriteriaDTO.getFactory() == null
                && productCriteriaDTO.getPrice() == null) {
            return this.productRepository.findAll(page); // Nếu không có tiêu chí, lấy tất cả
        }

        // Khởi tạo một Specification cho sản phẩm
        Specification<Product> combinedSpec = Specification.where(null);

        // Trả về danh sách sản phẩm phù hợp với Specification
        return this.productRepository.findAll(combinedSpec, page);
    }
}
