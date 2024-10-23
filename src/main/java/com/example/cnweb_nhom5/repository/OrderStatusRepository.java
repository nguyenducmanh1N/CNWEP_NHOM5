package com.example.cnweb_nhom5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cnweb_nhom5.domain.OrderStatus;
@Repository
public interface OrderStatusRepository extends  JpaRepository<OrderStatus, Long> {
    OrderStatus findByName(String name);
}
