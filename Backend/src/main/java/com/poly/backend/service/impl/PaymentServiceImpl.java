package com.poly.backend.service.impl;

import com.poly.backend.dao.OrderDAO;
import com.poly.backend.dao.OrderStatusDAO;
import com.poly.backend.dao.ProductVariantDAO;
import com.poly.backend.dto.CartItemRequestDTO;
import com.poly.backend.dto.CreatePaymentLinkRequestBody;
import com.poly.backend.entity.Order;
import com.poly.backend.entity.ProductVariant;
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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PayOS payOS;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderStatusDAO orderStatusDAO;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductVariantDAO productVariantDAO;

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

        // Lấy description từ Webhook (Vd: "TT 1774426564")
        String descriptionFromPayOS = data.getDescription();
        System.out.println("✅ Ting Ting! PayOS báo nhận được tiền. Webhook Description: " + descriptionFromPayOS);

        // 🔥 LƯU MÃ VÀO BỘ NHỚ TẠM (Bỏ chữ "TT " đi để chỉ còn số "1774426564")
        if (descriptionFromPayOS != null && descriptionFromPayOS.startsWith("TT ")) {
            String tempCode = descriptionFromPayOS.replace("TT ", "").trim();

            // Đánh dấu mã này là ĐÃ THANH TOÁN THÀNH CÔNG
            paidTransactions.put(tempCode, true);

            System.out.println("🔥 Đã lưu mã " + tempCode + " vào bộ nhớ. Frontend đang lấy data về để tạo đơn!");
        }

        // (Mình xóa cái đoạn tìm DB ở đây đi, vì lúc này đơn THẬT chưa được tạo, tìm sẽ báo lỗi đỏ console như bạn thấy)

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