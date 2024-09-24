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

        // Thêm tiêu chí target vào Specification nếu có
        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        // Thêm tiêu chí factory vào Specification nếu có
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        // Thêm tiêu chí price vào Specification nếu có
        if (productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()) {
            Specification<Product> currentSpecs = this.buildPriceSpecification(productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        // Trả về danh sách sản phẩm phù hợp với Specification
        return this.productRepository.findAll(combinedSpec, page);
    }
    // Xây dựng Specification cho tiêu chí giá
    public Specification<Product> buildPriceSpecification(List<String> price) {
        Specification<Product> combinedSpec = Specification.where(null); // Khởi tạo disjunction
        for (String p : price) {
            double min = 0;
            double max = 0;

            // Xác định min và max dựa trên chuỗi giá
            switch (p) {
                case "duoi-10-trieu":
                    min = 1;
                    max = 10000000;
                    break;
                case "10-15-trieu":
                    min = 10000000;
                    max = 15000000;
                    break;
                case "15-20-trieu":
                    min = 15000000;
                    max = 20000000;
                    break;
                case "tren-20-trieu":
                    min = 20000000;
                    max = 200000000;
                    break;
            }

            // Thêm tiêu chí giá vào Specification
            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec); // Sử dụng OR để kết hợp các tiêu chí
            }
        }

        return combinedSpec; // Trả về Specification kết hợp
    }

    // Lấy sản phẩm theo ID
    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }

    // Xóa sản phẩm theo ID
    public void deleteProduct(long id) {
        this.productRepository.deleteById(id);
    }
}
