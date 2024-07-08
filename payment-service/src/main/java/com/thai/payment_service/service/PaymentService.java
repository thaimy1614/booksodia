package com.thai.payment_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thai.payment_service.configuration.VNPAYConfig;
import com.thai.payment_service.dto.Order;
import com.thai.payment_service.dto.PaymentStatus;
import com.thai.payment_service.dto.response.VNPayResponse;
import com.thai.payment_service.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPAYConfig vnPayConfig;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public VNPayResponse createVnPayPayment(HttpServletRequest request) throws JsonProcessingException {
        String orderId = request.getParameter("orderId");
        Order order = objectMapper.readValue((String) redisTemplate.opsForValue().get("order:" + orderId), Order.class);
        if (order == null) {
            return VNPayResponse.builder().code("701").message("Your order is not created, please try again").build();
        } else if (!order.getStatus().toString().equals("PENDING")) {
            return VNPayResponse.builder().code("700").message("This order is not pending. Please try again later.").build();
        }
        long amount = order.getTotalAmount() * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_TxnRef", orderId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_OrderInfo", "Your order id is " + orderId + ", total price is " + amount);
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return VNPayResponse.builder().code("ok").message("success").paymentUrl(paymentUrl).build();
    }

    public VNPayResponse handleCallback(String status, HttpServletRequest request) {
        String orderId = request.getParameter("vnp_TxnRef");
        kafkaTemplate.send("payment-status", PaymentStatus.builder().status(status).orderId(orderId).build());
        if (status.equals("00")) {
            return new VNPayResponse("00", "Success", request.getParameter("vnp_OrderInfo"));
        } else {
            return null;
        }
    }
}
