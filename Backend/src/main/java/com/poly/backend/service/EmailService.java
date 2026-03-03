package com.poly.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Chạy bất đồng bộ để không làm chậm quá trình đặt hàng của user
    public void sendOrderConfirmation(String toEmail, String orderCode, String totalAmount) {
        if (toEmail == null || toEmail.trim().isEmpty()) return;

        new Thread(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("TechZone Store <no-reply@techzone.com>");
                message.setTo(toEmail);
                message.setSubject("Xác nhận đặt hàng thành công - " + orderCode);
                message.setText("Chào bạn,\n\n"
                        + "Cảm ơn bạn đã mua sắm tại TechZone Store!\n"
                        + "Mã đơn hàng của bạn là: " + orderCode + "\n"
                        + "Tổng thanh toán: " + totalAmount + " VNĐ\n\n"
                        + "Chúng tôi sẽ sớm liên hệ để giao hàng.\n\n"
                        + "Trân trọng,\nTechZone Store");

                mailSender.send(message);
            } catch (Exception e) {
                System.err.println("Lỗi gửi email: " + e.getMessage());
            }
        }).start();
    }
}