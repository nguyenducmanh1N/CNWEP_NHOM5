package com.example.cnweb_nhom5.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

    private final ProductService productService;
    private final PaymentService paymentService;
    public PaymentController( ProductService productService, PaymentService paymentService){
        this.productService = productService;
        this.paymentService = paymentService;
    }
    
    @GetMapping("/create_payment")
    // ResponseEntity<?>
    public void createPayment(
        @RequestParam double totalPrice,
        HttpServletResponse response,
        HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone,
            @RequestParam("paymentId") Long paymentId
            ) throws IOException  {

        System.out.println("Total Price: " + totalPrice);
        // String orderType = req.getParameter("other");
        // long amount = Integer.parseInt(req.getParameter("amount"))*100;
        // String bankCode = req.getParameter("bankCode");
        // public static String vnp_Version = "2.1.0";
        // public static String vnp_Command = "pay";
        long amount = (long) (totalPrice * 100);
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        // String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;