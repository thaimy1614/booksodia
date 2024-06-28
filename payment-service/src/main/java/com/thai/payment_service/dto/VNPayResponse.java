package com.thai.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }