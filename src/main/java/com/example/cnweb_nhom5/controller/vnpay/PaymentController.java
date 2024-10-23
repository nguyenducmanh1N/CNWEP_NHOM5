package com.example.cnweb_nhom5.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cnweb_nhom5.config.VNPayConfig;
import com.example.cnweb_nhom5.domain.Cart;
import com.example.cnweb_nhom5.domain.CartDetail;
import com.example.cnweb_nhom5.domain.Payment;
import com.example.cnweb_nhom5.domain.Product;
import com.example.cnweb_nhom5.domain.User;
import com.example.cnweb_nhom5.domain.dto.PaymentInfoVNPAYDTO;
import com.example.cnweb_nhom5.domain.dto.PaymentRestDTO;
import com.example.cnweb_nhom5.domain.dto.TransactionStatusDTO;
import com.example.cnweb_nhom5.service.PaymentService;
import com.example.cnweb_nhom5.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

    private final ProductService productService;
    private final PaymentService paymentService;

    public PaymentController(ProductService productService, PaymentService paymentService) {
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
            @RequestParam("paymentId") Long paymentId) throws IOException {

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

        PaymentInfoVNPAYDTO payment_info = new PaymentInfoVNPAYDTO(paymentId, receiverName, receiverAddress,
                receiverPhone);

        String payment_info_str = mapper.writeValueAsString(payment_info);

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");

        // if (bankCode != null && !bankCode.isEmpty()) {
        // vnp_Params.put("vnp_BankCode", bankCode);
        // }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", payment_info_str);
        vnp_Params.put("vnp_Locale", "vn");
        // vnp_Params.put("vnp_OrderType", orderType);
        String orderType = "other";
        vnp_Params.put("vnp_OrderType", orderType);

        // String locate = req.getParameter("language");
        // if (locate != null && !locate.isEmpty()) {
        // vnp_Params.put("vnp_Locale", locate);
        // } else {
        // vnp_Params.put("vnp_Locale", "vn");
        // }
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);

        vnp_Params.put("vnp_IpAddr", "192.168.2.12");
        // vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        // PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
        // paymentRestDTO.setStatus("OKE");
        // paymentRestDTO.setMessage("successfully");
        // paymentRestDTO.setUrl(paymentUrl);

        // com.google.gson.JsonObject job = new JsonObject();
        // job.addProperty("code", "00");
        // job.addProperty("message", "success");
        // job.addProperty("data", paymentUrl);
        // Gson gson = new Gson();
        // resp.getWriter().write(gson.toJson(job));

        // return ResponseEntity.status(HttpStatus.OK).body(paymentRestDTO);
        // + "&receiverName=" + receiverName + "&receiverAddress=" + receiverAddress
        // +"&receiverPhone="+ receiverPhone+"&paymentId="+ paymentId
        System.out.println("Receiver Name: " + receiverName);
        System.out.println("Receiver Address: " + receiverAddress);
        System.out.println("Receiver Phone: " + receiverPhone);
        System.out.println("Payment ID: " + paymentId);
        response.sendRedirect(paymentUrl);
    }

    @GetMapping("/payment_infor")
    public String handleTransaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,

            HttpServletRequest request) {

        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
        HttpSession session = request.getSession(false);
        User currentUser = null;

        if (session != null && session.getAttribute("id") != null) {
            long id = (long) session.getAttribute("id");
            currentUser = new User();
            currentUser.setId(id);
        }

        // ObjectMapper mapper = new ObjectMapper();
        // try {
        // PaymentInfoVNPAYDTO paymentInfo = mapper.readValue(order,
        // PaymentInfoVNPAYDTO.class);
        // System.out.println("PAYMENT INFO:" + paymentInfo);

        // } catch (JsonMappingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (JsonProcessingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // if (responseCode.equals("00")) {

        // Cart cart = this.productService.fetchByUser(currentUser);
        // List<CartDetail> cartDetails = cart.getCartDetails();

        // // Duyệt qua từng sản phẩm trong giỏ hàng
        // for (CartDetail cartDetail : cartDetails) {
        // Product product = cartDetail.getProduct();

        // // Cập nhật số lượng sold và quantity của sản phẩm
        // long quantityBought = cartDetail.getQuantity();
        // product.setSold(product.getSold() + quantityBought);
        // product.setQuantity(product.getQuantity() - quantityBought);

        // // Lưu thay đổi vào database
        // productService.createProduct(product);
        // }

        // this.productService.handlePlaceOrder(currentUser, session,
        // paymentInfo.getReceiverName(),
        // paymentInfo.getReceiverAddress(),
        // paymentInfo.getReceiverPhone(),
        // paymentInfo.getPaymentId());
        // } else {
        // transactionStatusDTO.setStatus("no");
        // transactionStatusDTO.setMessage("failed");
        // transactionStatusDTO.setData("");

        // }
        if (responseCode.equals("00")) {
            transactionStatusDTO.setStatus("OK");
            transactionStatusDTO.setMessage("Successfully");
            transactionStatusDTO.setData("");

            ObjectMapper mapper = new ObjectMapper();
            try {
                // Lấy thông tin từ vnp_OrderInfo (order)
                PaymentInfoVNPAYDTO paymentInfo = mapper.readValue(order, PaymentInfoVNPAYDTO.class);
                System.out.println("PAYMENT INFO: " + paymentInfo);

                // Sử dụng thông tin từ paymentInfo để đặt hàng
                Cart cart = this.productService.fetchByUser(currentUser);
                List<CartDetail> cartDetails = cart.getCartDetails();

                // Duyệt qua từng sản phẩm trong giỏ hàng
                for (CartDetail cartDetail : cartDetails) {
                    Product product = cartDetail.getProduct();

                    // Cập nhật số lượng sold và quantity của sản phẩm
                    long quantityBought = cartDetail.getQuantity();
                    product.setSold(product.getSold() + quantityBought);
                    product.setQuantity(product.getQuantity() - quantityBought);

                    // Lưu thay đổi vào database
                    productService.createProduct(product);
                }

                // Gọi phương thức lưu thông tin đơn hàng với thông tin từ PaymentInfoVNPAYDTO
                this.productService.handlePlaceOrder(
                        currentUser, session,
                        paymentInfo.getReceivedName(),
                        paymentInfo.getReceiverAddress(),
                        paymentInfo.getReceivedPhone(),
                        paymentInfo.getPaymentId());
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            transactionStatusDTO.setStatus("no");
            transactionStatusDTO.setMessage("failed");
            transactionStatusDTO.setData("");
        }
        return "redirect:/thanks";
    }
}