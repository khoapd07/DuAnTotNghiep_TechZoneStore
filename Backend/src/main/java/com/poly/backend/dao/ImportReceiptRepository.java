package com.poly.backend.dao;

import com.poly.backend.entity.ImportReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportReceiptRepository extends JpaRepository<ImportReceipt, Integer> {
    // Có thể viết thêm hàm tìm kiếm theo mã phiếu nhập ở đây
    ImportReceipt findByReceiptCode(String receiptCode);

    // Hàm này để đếm xem Nhà Cung Cấp (theo ID) đã có phiếu nhập nào liên kết chưa
    boolean existsBySupplier_SupplierId(Integer supplierId);
}