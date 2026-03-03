package com.poly.backend.dto;
import lombok.Data;
import java.util.List;

@Data
public class GuestOrderRequestDTO {
    private String note;
    private String voucherCode;

    // Thông tin khách vãng lai để lưu vào note hoặc có thể tạo bảng riêng sau này
    private String guestFullName;
    private String guestPhone;
    private String guestEmail;
    private String guestAddress;

    // Bắt buộc phải có list này vì khách vãng lai ko có giỏ hàng trong Database
    private List<GuestCartItemDTO> items;
}