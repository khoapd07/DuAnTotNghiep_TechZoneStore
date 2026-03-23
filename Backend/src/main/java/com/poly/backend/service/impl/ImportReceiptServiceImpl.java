package com.poly.backend.service.impl;

import com.poly.backend.dao.ImportReceiptDAO;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.dao.ProductVariantDAO; // THÊM IMPORT NÀY
import com.poly.backend.entity.Product;
import com.poly.backend.entity.ProductVariant; // THÊM IMPORT NÀY

import com.poly.backend.dto.ImportReceiptDetailDTO;
import com.poly.backend.dto.ImportReceiptListDTO;
import com.poly.backend.entity.ImportReceipt;
import com.poly.backend.entity.ImportReceiptDetail;
import com.poly.backend.service.ImportReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImportReceiptServiceImpl implements ImportReceiptService {

    @Autowired
    private ImportReceiptDAO importReceiptDAO;

    @Autowired
    private ProductDAO productDAO;

    // --- BỔ SUNG: Tiêm DAO của Phân loại vào ---
    @Autowired
    private ProductVariantDAO productVariantDAO;

    private final SimpleDateFormat sdfList = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat sdfDetail = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public List<ImportReceipt> getAllReceipts() {
        return importReceiptDAO.findAll();
    }

    @Override
    public ImportReceipt getReceiptById(Integer id) {
        return importReceiptDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public ImportReceipt createReceipt(ImportReceipt receipt) {
        if (receipt.getReceiptDate() == null) {
            receipt.setReceiptDate(new Date());
        }

        if (receipt.getReceiptCode() == null || receipt.getReceiptCode().trim().isEmpty()) {
            receipt.setReceiptCode("PN-" + System.currentTimeMillis());
        }

        if (receipt.getDetails() != null) {
            for (ImportReceiptDetail detail : receipt.getDetails()) {
                detail.setImportReceipt(receipt);

                // 1. Kiểm tra Sản phẩm thật
                if (detail.getProduct() != null && detail.getProduct().getProductId() != null) {
                    Product realProduct = productDAO.findById(detail.getProduct().getProductId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có ID: " + detail.getProduct().getProductId()));
                    detail.setProduct(realProduct);
                }

                // 2. BỔ SUNG: Kiểm tra Phân loại (Variant) thật (NẾU CÓ CHỌN)
                if (detail.getVariant() != null && detail.getVariant().getVariantId() != null) {
                    ProductVariant realVariant = productVariantDAO.findById(detail.getVariant().getVariantId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy phân loại sản phẩm có ID: " + detail.getVariant().getVariantId()));
                    detail.setVariant(realVariant);
                }
            }
        }
        return importReceiptDAO.save(receipt);
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "AD";
        String[] words = fullName.trim().split("\\s+");
        if (words.length == 1) return words[0].substring(0, 1).toUpperCase();
        return (words[0].substring(0, 1) + words[words.length - 1].substring(0, 1)).toUpperCase();
    }

    @Override
    public List<ImportReceiptListDTO> getAllReceiptsForList() {
        return importReceiptDAO.findAll().stream().map(receipt -> {
            ImportReceiptListDTO dto = new ImportReceiptListDTO();
            dto.setId(receipt.getReceiptId());
            dto.setCode(receipt.getReceiptCode());
            dto.setDate(receipt.getReceiptDate() != null ? sdfList.format(receipt.getReceiptDate()) : "");
            dto.setTotal(receipt.getTotalMoney());

            String empName = receipt.getEmployee() != null ? receipt.getEmployee().getFullName() : "Admin";
            dto.setCreatorName(empName);
            dto.setCreatorInitials(getInitials(empName));
            dto.setSupplier(receipt.getSupplier() != null ? receipt.getSupplier().getSupplierName() : "N/A");
            dto.setNote(receipt.getNote());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ImportReceiptDetailDTO getReceiptDetail(Integer id) {
        ImportReceipt receipt = importReceiptDAO.findById(id).orElse(null);
        if (receipt == null) return null;

        ImportReceiptDetailDTO dto = new ImportReceiptDetailDTO();
        dto.setReceiptCode(receipt.getReceiptCode());
        dto.setSupplierName(receipt.getSupplier() != null ? receipt.getSupplier().getSupplierName() : "N/A");
        dto.setReceiptDate(receipt.getReceiptDate() != null ? sdfDetail.format(receipt.getReceiptDate()) : "");
        dto.setEmployeeName(receipt.getEmployee() != null ? receipt.getEmployee().getFullName() : "Admin");
        dto.setTotalValue(receipt.getTotalMoney());
        dto.setNote(receipt.getNote());

        List<ImportReceiptDetailDTO.ImportReceiptItemDTO> items = receipt.getDetails().stream().map(detail -> {
            ImportReceiptDetailDTO.ImportReceiptItemDTO itemDTO = new ImportReceiptDetailDTO.ImportReceiptItemDTO();

            itemDTO.setName(detail.getProduct().getName());

            // --- ĐÃ SỬA LẠI CHUẨN XÁC: KẾT HỢP COLOR + OPTION2 VÀ MÃ SKU ---
            if (detail.getVariant() != null) {
                // 1. Gộp tên phân loại (Ví dụ: "Đen - 128GB")
                String vName = detail.getVariant().getColorName();
                if (detail.getVariant().getOption2Value() != null && !detail.getVariant().getOption2Value().isEmpty()) {
                    vName += " - " + detail.getVariant().getOption2Value();
                }
                itemDTO.setVariantName(vName);

                // 2. Lấy mã SKU
                if (detail.getVariant().getSkuCode() != null) {
                    itemDTO.setCode(detail.getVariant().getSkuCode());
                } else {
                    itemDTO.setCode("SP-" + detail.getProduct().getProductId());
                }

                // 3. Lấy ảnh biến thể (Nếu ko có thì lấy ảnh sản phẩm gốc)
                String img = detail.getVariant().getImageUrl();
                if (img == null || img.isEmpty()) {
                    img = detail.getProduct().getImageUrl();
                }
                itemDTO.setImage((img != null && !img.isEmpty()) ? img : "https://via.placeholder.com/50");

            } else {
                // NẾU SẢN PHẨM KHÔNG CÓ PHÂN LOẠI
                itemDTO.setVariantName("Mặc định");
                itemDTO.setCode("SP-" + detail.getProduct().getProductId());
                String img = detail.getProduct().getImageUrl();
                itemDTO.setImage((img != null && !img.isEmpty()) ? img : "https://via.placeholder.com/50");
            }

            itemDTO.setQuantity(detail.getQuantity());
            itemDTO.setImportPrice(detail.getImportPrice());

            if (detail.getLineTotal() == null && detail.getQuantity() != null && detail.getImportPrice() != null) {
                itemDTO.setLineTotal(detail.getQuantity() * detail.getImportPrice());
            } else {
                itemDTO.setLineTotal(detail.getLineTotal());
            }

            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }

    @Override
    @Transactional
    public ImportReceipt updateReceipt(Integer id, ImportReceipt receiptDetails) {
        ImportReceipt existing = importReceiptDAO.findById(id).orElse(null);
        if (existing != null) {
            existing.setNote(receiptDetails.getNote());
            return importReceiptDAO.save(existing);
        }
        return null;
    }

    @Override
    @Transactional
    public void cancelReceipt(Integer id) {
        ImportReceipt existing = importReceiptDAO.findById(id).orElse(null);
        if (existing != null) {
            importReceiptDAO.delete(existing);
        }
    }
}