package com.poly.backend.dao;

import com.poly.backend.entity.ImportReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportReceiptDAO extends JpaRepository<ImportReceipt, Integer> {
    // Có thể viết thêm hàm tìm kiếm theo mã phiếu nhập ở đây
    ImportReceipt findByReceiptCode(String receiptCode);
}