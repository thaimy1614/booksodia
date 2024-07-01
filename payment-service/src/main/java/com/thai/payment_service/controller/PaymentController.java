package com.thai.payment_service.controller;

import com.thai.payment_service.dto.response.VNPayResponse;
import com.thai.payment_service.model.Order;
import com.thai.payment_service.service.OrderService;
import com.thai.payment_service.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @GetMapping("/pay")
    public VNPayResponse pay(HttpServletRequest request) {
        return paymentService.createVnPayPayment(request);
    }

    @GetMapping("/vn-pay-callback")
    public VNPayResponse payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_OrderInfo");
        if (status.equals("00")) {
            orderService.updateOrder(Order.Status.DONE, orderId);
            return new VNPayResponse("00", "Success", "");
        } else {
            return null;
        }
    }
}
