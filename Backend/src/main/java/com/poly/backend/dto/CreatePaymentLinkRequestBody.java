package com.poly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentLinkRequestBody {
    private String orderCode;
    private String productName;
    private String description;
    private String returnUrl;
    private String cancelUrl;
    private int price;
}