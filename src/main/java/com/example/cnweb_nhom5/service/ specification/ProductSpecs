package com.example.cnweb_nhom5.service.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.domain.Product_;

import jakarta.persistence.criteria.Join;

public class ProductSpecs {
    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    }

    // case 1
    public static Specification<Product> minPrice(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get(Product_.PRICE), price);
    }

    // case 2
    public static Specification<Product> maxPrice(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get(Product_.PRICE), price);
    }

    // case3
    public static Specification<Product> matchFactory(String factory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.FACTORY), factory);
    }

    // case4
    // public static Specification<Product> matchListFactory(List<String> factory) {
    //     return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.FACTORY)).value(factory);
    // }

    // case4
    // public static Specification<Product> matchListTarget(List<String> target) {
    //     return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.TARGET)).value(target);
    // }
    public static Specification<Product> matchListTarget(List<String> targetNames) {
        return (root, query, criteriaBuilder) -> {
            // Join bảng Target với Product
            Join targetJoin = root.join("target");
            // So sánh trường 'name' của Target với danh sách targetNames
            return criteriaBuilder.in(targetJoin.get("name")).value(targetNames);
        };
    }

    public static Specification<Product> matchListFactory(List<String> factoryNames) {
        return (root, query, criteriaBuilder) -> {
            // Join bảng Factory với Product
            Join factoryJoin = root.join("factory");
            // So sánh trường 'name' của Factory với danh sách factoryNames
            return criteriaBuilder.in(factoryJoin.get("name")).value(factoryNames);
        };
    }

    // case5
    public static Specification<Product> matchPrice(double min, double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.gt(root.get(Product_.PRICE), min),
                criteriaBuilder.le(root.get(Product_.PRICE), max));
    }

    // case6
    public static Specification<Product> matchMultiplePrice(double min, double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                root.get(Product_.PRICE), min, max);
    }

    
}
