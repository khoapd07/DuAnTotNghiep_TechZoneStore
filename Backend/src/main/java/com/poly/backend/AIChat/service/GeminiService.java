package com.poly.backend.AIChat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.poly.backend.entity.Product;
import com.poly.backend.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProductRepository productRepository;

    // =========================================================================
    // TƯ DUY GỐC DÀNH CHO ĐỒ ÁN (Phiên bản Chuyên viên CSKH Full Kỹ Năng)
    // =========================================================================
    private final String SYSTEM_PROMPT =
            "Bạn là trợ lý ảo CSKH cực kỳ chuyên nghiệp, tận tâm và am hiểu nghiệp vụ của hệ thống TechZone. " +
                    "THÔNG TIN LIÊN HỆ: Website: techzone.... | Hotline: 0939079015 | Zalo: 0939079015 (TechZone Support).| Fanpage : chưa có " +
                    "QUY TẮC GIAO TIẾP: Luôn chào hỏi lễ phép, xưng hô 'dạ/vâng', gọi khách là 'anh/chị' hoặc 'bạn'. Trả lời súc tích, ngắt dòng rõ ràng cho dễ đọc và luôn kèm emoji thân thiện. " +

                    "KIẾN THỨC NGHIỆP VỤ CSKH (HÃY DÙNG ĐỂ GIẢI ĐÁP CHO KHÁCH): " +
                    "1. Hướng dẫn đặt hàng: Dạ anh/chị chỉ cần chọn sản phẩm, bấm 'Mua ngay' hoặc 'Thêm vào giỏ hàng', sau đó điền thông tin nhận hàng là hoàn tất ạ. " +
                    "2. Phương thức thanh toán: TechZone hỗ trợ 2 hình thức: (1) Thanh toán tiền mặt khi nhận hàng (COD), (2) Chuyển khoản ngân hàng siêu tiện lợi qua mã QR. " +
                    "3. Phí ship & Giao hàng: Shop giao hàng toàn quốc (thời gian 1-3 ngày tùy khu vực). Phí ship sẽ được tính tự động và hiển thị rõ ràng ở bước Thanh toán ạ. " +
                    "4. Đổi trả / Bảo hành: TechZone áp dụng chính sách Lỗi 1 đổi 1 trong 30 ngày đầu tiên. Sau đó sản phẩm được bảo hành chính hãng theo đúng quy định của nhà sản xuất. " +
                    "5. Kiểm tra đơn hàng: Khách có thể đăng nhập vào tài khoản trên web để tra cứu, hoặc cung cấp 'Mã đơn hàng' để em kiểm tra giúp mình nhé. " +
                    "6. Gặp nhân viên / Nhắn Zalo: Nếu khách yêu cầu gặp nhân viên thật hoặc xin Zalo, hãy cung cấp số Zalo và nói: 'Dạ vâng, anh/chị đợi em một chút xíu, nhân viên trực page sẽ nhảy vào hỗ trợ mình ngay bây giờ ạ!'. " +

                    "QUY TẮC BÁN HÀNG QUAN TRỌNG (BẮT BUỘC TUÂN THỦ): " +
                    "1. KIỂM TRA GIÁ NGHIÊM NGẶT: Người Việt hay gọi tắt '5k' là 5.000 VNĐ, '5 củ' là 5.000.000 VNĐ. Nếu khách nói một con số như '5000', BẮT BUỘC phải tìm đúng sản phẩm có giá 5000 VNĐ. KHÔNG ĐƯỢC tự suy diễn 5000 là 5 triệu. Nếu không có sản phẩm nào giá đó, hãy lịch sự báo cho khách biết. " +
                    "2. CHỈ BÁN HÀNG TRONG DANH SÁCH: Tuyệt đối không tự bịa ra sản phẩm. Nếu khách hỏi sản phẩm không có, hãy báo hết hàng và gợi ý mẫu khác trong danh sách. " +
                    "3. BẮT BUỘC KÈM LINK: Khi giới thiệu bất kỳ sản phẩm nào, PHẢI chèn kèm theo đường Link chi tiết của sản phẩm đó để khách bấm vào.";

    public String askGemini(String userMessage) {
        try {
            String requestUrl = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String contextData = "";

            // Lấy TẤT CẢ sản phẩm từ Database (Do DB đồ án ít sản phẩm nên dùng findAll là an toàn và dễ nhất)
            List<Product> products = productRepository.findAll();

            if (!products.isEmpty()) {
                contextData = "\n\n[DANH SÁCH SẢN PHẨM HIỆN CÓ TẠI SHOP]:\n";
                for (Product p : products) {
                    // Tạo link chi tiết cho Vue.js
                    String productUrl = "http://localhost:5173/product/" + p.getProductId();

                    // Nối tên, giá và link vào "phao thi"
                    contextData += "- Tên: " + p.getName() + " | Giá: " + p.getPrice() + " VNĐ | Link: " + productUrl + "\n";
                }
            } else {
                contextData = "\n\n[LƯU Ý]: Hiện tại cửa hàng chưa có sản phẩm nào trong kho.";
            }

            // Ghép Tư duy gốc + Danh sách sản phẩm + Câu hỏi của khách
            String fullPrompt = SYSTEM_PROMPT + contextData + "\n\nKhách hàng nói: " + userMessage + "\nTrợ lý ảo trả lời:";

            // Cấu trúc JSON gửi lên Google
            Map<String, Object> parts = new HashMap<>();
            parts.put("text", fullPrompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(parts));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(content));

            // Bắn Request lên Google (Chỉ gọi đúng 1 lần)
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, entity, String.class);

            // Đọc kết quả JSON
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Dạ hiện tại hệ thống AI bên em đang quá tải chút xíu, anh/chị chờ em một lát để em báo nhân viên hỗ trợ mình nhé! 🙏";
        }
    }
}