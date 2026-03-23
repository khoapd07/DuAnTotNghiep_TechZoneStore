package com.poly.backend.service;

import com.poly.backend.dto.ImportReceiptDetailDTO;
import com.poly.backend.dto.ImportReceiptListDTO;
import com.poly.backend.entity.ImportReceipt;
import java.util.List;

public interface ImportReceiptService {
    // ----------------------------------------------------
    // CÁC HÀM CŨ DÙNG ENTITY (Dành cho việc lưu data)
    // ----------------------------------------------------
    List<ImportReceipt> getAllReceipts();
    ImportReceipt getReceiptById(Integer id);
    ImportReceipt createReceipt(ImportReceipt receipt);

    // ----------------------------------------------------
    // CÁC HÀM MỚI DÙNG DTO (Dành cho hiển thị lên Vue.js)
    // ----------------------------------------------------
    List<ImportReceiptListDTO> getAllReceiptsForList();
    ImportReceiptDetailDTO getReceiptDetail(Integer id);
    ImportReceipt updateReceipt(Integer id, ImportReceipt receiptDetails);
    void cancelReceipt(Integer id);
}