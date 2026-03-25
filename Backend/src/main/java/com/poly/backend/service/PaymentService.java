package com.poly.backend.service;

import com.poly.backend.dto.CreatePaymentLinkRequestBody;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.webhooks.WebhookData;

public interface PaymentService {
    CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequestBody request) throws Exception;

    WebhookData processWebhook(Object body) throws Exception;

    void cancelOrderFromQR(String orderCode) throws Exception;

    Boolean checkPaymentStatus(String orderCode);
}