package com.thai.payment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thai.payment_service.dto.ResponseObject;
import com.thai.payment_service.dto.response.VNPayResponse;
import com.thai.payment_service.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VnPayController {
    private final PaymentService paymentService;
    private final

    @GetMapping("/get-url")
    ResponseObject<VNPayResponse> payCart(
            HttpServletRequest servletRequest
    ) throws JsonProcessingException {
        VNPayResponse response = paymentService.createVnPayPayment(servletRequest);

        return new ResponseObject<>(
                HttpStatus.OK,
                "Success",
                response
        );
    }

    @GetMapping("/vn-pay-callback")
    public ResponseObject<VNPayResponse> payCallbackHandler(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String status = request.getParameter("vnp_ResponseCode");
        VNPayResponse vnPayResponse = paymentService.handleCallback(status, request);
        return new ResponseObject<>(HttpStatus.OK, "Success", vnPayResponse);
    }
}
