package com.poly.backend.controller;

import com.poly.backend.dto.ApiResponse;
import com.poly.backend.dto.CreatePaymentLinkRequestBody;
import com.poly.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.webhooks.WebhookData;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 1. API TẠO LINK THANH TOÁN (Frontend VueJS gọi)
     */
    @PostMapping("/create")
    public ApiResponse<CreatePaymentLinkResponse> createPaymentLink(@RequestBody CreatePaymentLinkRequestBody request) {
        try {
            CreatePaymentLinkResponse data = paymentService.createPaymentLink(request);
            return ApiResponse.success(data); // Trả về data chứa checkoutUrl để VueJS redirect
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Lỗi tạo link thanh toán: " + e.getMessage());
        }
    }

    /**
     * 2. API WEBHOOK (PayOS tự động gọi ngầm vào đây khi khách quét QR thành công)
     */
    @PostMapping(path = "/payos_transfer_handler")
    public ApiResponse<WebhookData> payosTransferHandler(@RequestBody Object body) {
        try {
            WebhookData data = paymentService.processWebhook(body);
            return ApiResponse.success(data);
        } catch (Exception e) {
            System.err.println("❌ Lỗi xử lý Webhook PayOS: " + e.getMessage());
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 3. API HỦY ĐƠN HÀNG (Frontend VueJS gọi khi khách bấm Hủy thanh toán)
     */
    @DeleteMapping("/cancel-qr/{orderCode}")
    public ApiResponse<?> cancelOrderFromQR(@PathVariable String orderCode) {
        try {
            paymentService.cancelOrderFromQR(orderCode);
            return ApiResponse.success("Hủy thành công và khôi phục giỏ hàng");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Lỗi xóa đơn: " + e.getMessage());
        }
    }

    /**
     * 4. API KIỂM TRA TRẠNG THÁI (Dự phòng)
     */
    @GetMapping("/check-status/{orderCode}")
    public ApiResponse<Boolean> checkPaymentStatus(@PathVariable String orderCode) {
        try {
            Boolean status = paymentService.checkPaymentStatus(orderCode);
            return ApiResponse.success(status);
        } catch (Exception e) {
            return ApiResponse.success(false);
        }
    }
}