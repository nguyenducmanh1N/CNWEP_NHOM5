package com.example.cnweb_nhom5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

    private final ProductService productService;
    private final PaymentService paymentService;
    public PaymentController( ProductService productService, PaymentService paymentService){
        this.productService = productService;
        this.paymentService = paymentService;
    }