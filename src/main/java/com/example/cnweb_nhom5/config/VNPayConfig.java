package com.example.cnweb_nhom5.config;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.security.*;

public class VNPayConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:8080/api/payment/payment_infor";
    public static String vnp_TmnCode = "6W3GVDBC";
    public static String secretKey = "303ER5WHDE43OZXUCL9BMEOKLZ0CYBVS";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    //mk 123456Mm
     public static String md5(String message) {
         String digest = null;
         try {
             MessageDigest md = MessageDigest.getInstance("MD5");
             byte[] hash = md.digest(message.getBytes("UTF-8"));
             StringBuilder sb = new StringBuilder(2 * hash.length);
             for (byte b : hash) {
                 sb.append(String.format("%02x", b & 0xff));
             }
             digest = sb.toString();
         } catch (UnsupportedEncodingException ex) {
             digest = "";
         } catch (NoSuchAlgorithmException ex) {
             digest = "";
         }
         return digest;
     }
}
