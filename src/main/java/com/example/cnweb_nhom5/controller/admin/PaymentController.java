package com.example.cnweb_nhom5.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        
        // Map<String, Object> payment_info = new HashMap<>();
        // payment_info.put("payment_id", paymentId);
        ObjectMapper mapper = new ObjectMapper();

        PaymentInfoVNPAYDTO payment_info = new PaymentInfoVNPAYDTO(paymentId, receiverName,receiverAddress,receiverPhone);
        
        String payment_info_str = mapper.writeValueAsString(payment_info);


        Map<String, String> vnp_Params = new HashMap<>();
         
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        