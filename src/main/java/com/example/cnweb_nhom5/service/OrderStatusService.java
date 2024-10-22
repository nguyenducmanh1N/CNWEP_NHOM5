package com.example.cnweb_nhom5.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cnweb_nhom5.domain.OrderStatus;
import com.example.cnweb_nhom5.repository.OrderStatusRepository;
@Service
public class OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;
    public  OrderStatusService (OrderStatusRepository orderStatusRepository){
        this.orderStatusRepository = orderStatusRepository;
        
    }
    public List<OrderStatus> fetchAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }
}
