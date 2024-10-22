package com.example.cnweb_nhom5.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cnweb_nhom5.domain.Review;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByProductId(Long productId);
}
