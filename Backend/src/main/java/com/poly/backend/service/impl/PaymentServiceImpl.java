package com.poly.backend.service.impl;

import com.poly.backend.repository.OrderRepository;
import com.poly.backend.repository.OrderStatusRepository;
import com.poly.backend.repository.ProductVariantRepository;
import com.poly.backend.dto.CreatePaymentLinkRequestBody;
import com.poly.backend.service.CartService;
import com.poly.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.webhooks.WebhookData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PayOS payOS;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    // 🔥 TUYỆT CHIÊU: BỘ NHỚ TẠM ĐỂ WEBHOOK BÁO TIN CHO FRONTEND
    private final Map<String, Boolean> paidTransactions = new ConcurrentHashMap<>();

    public PaymentServiceImpl(PayOS payOS) {
        this.payOS = payOS;
    }

    @Override
    public CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequestBody request) throws Exception {

        long payosOrderCode = Long.parseLong(String.valueOf(request.getOrderCode()));

        PaymentLinkItem item = PaymentLinkItem.builder()
                .name(request.getProductName())
                .quantity(1)
                .price((long) request.getPrice())
                .build();

        String paymentDescription = request.getDescription();
        if (paymentDescription.length() > 25) {
            paymentDescription = paymentDescription.substring(0, 25);
        }

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(payosOrderCode)
                .description(paymentDescription)
                .amount((long) request.getPrice())
                .item(item)
                .returnUrl(request.getReturnUrl())
                .cancelUrl(request.getCancelUrl())
                .build();

        return payOS.paymentRequests().create(paymentData);
    }

    @Override
    @Transactional
    public WebhookData processWebhook(Object body) throws Exception {
        WebhookData data = payOS.webhooks().verify(body);

        System.out.println("✅ Ting Ting! PayOS báo nhận được tiền.");

        // Lấy trực tiếp orderCode dạng số nguyên từ PayOS trả về
        Long payosOrderCode = data.getOrderCode();

        if (payosOrderCode != null) {
            // Chuyển lại thành String để map với frontend
            String tempCode = String.valueOf(payosOrderCode);

            // Đánh dấu mã này là ĐÃ THANH TOÁN THÀNH CÔNG
            paidTransactions.put(tempCode, true);

            System.out.println("🔥 Đã lưu mã " + tempCode + " vào bộ nhớ. Frontend đang lấy data về để tạo đơn!");
        }

        return data;
    }

    @Override
    public Boolean checkPaymentStatus(String orderCode) {
        // 🔥 FRONTEND CHỈ CẦN CHECK BỘ NHỚ TẠM (BỎ QUA LUÔN CÁI SDK BỊ LỖI CỦA PAYOS)
        // Nếu Webhook đã cất mã vào bộ nhớ -> Trả về true -> Vue.js nhảy trang tạo đơn!
        return paidTransactions.getOrDefault(orderCode, false);
    }

    @Override
    @Transactional
    public void cancelOrderFromQR(String orderCode) throws Exception {
        // Hàm hủy này giờ không cần làm gì ở DB nữa vì đơn nháp đã không còn.
        // Chỉ cần xóa mã khỏi bộ nhớ tạm nếu khách hủy
        paidTransactions.remove(orderCode);
        System.out.println("🗑️ Đã hủy mã QR tạm thời: " + orderCode);
    }
}