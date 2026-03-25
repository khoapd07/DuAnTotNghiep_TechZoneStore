package com.poly.backend.service.impl;

import com.poly.backend.dao.ImportReceiptDAO;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.dao.ProductVariantDAO;
import com.poly.backend.dto.ImportReceiptDetailDTO;
import com.poly.backend.dto.ImportReceiptListDTO;
import com.poly.backend.entity.ImportReceipt;
import com.poly.backend.entity.ImportReceiptDetail;
import com.poly.backend.entity.Product;
import com.poly.backend.entity.ProductVariant;
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

    @Autowired
    private ProductVariantDAO productVariantDAO;

    // ==========================================
    //         CÁC HÀM XỬ LÝ CHÍNH (PUBLIC)
    // ==========================================

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
        // Khởi tạo ngày và mã phiếu mặc định nếu client không gửi
        if (receipt.getReceiptDate() == null) receipt.setReceiptDate(new Date());
        if (receipt.getReceiptCode() == null || receipt.getReceiptCode().trim().isEmpty()) {
            receipt.setReceiptCode("PN-" + System.currentTimeMillis());
        }

        if (receipt.getDetails() != null) {
            for (ImportReceiptDetail detail : receipt.getDetails()) {
                detail.setImportReceipt(receipt);

                // 1. Lấy Sản phẩm thật từ DB
                Product product = getProductOrThrow(detail.getProduct().getProductId());
                detail.setProduct(product);

                // Tính toán Thành tiền (line_total) phòng trường hợp frontend gửi thiếu
                if (detail.getLineTotal() == null && detail.getQuantity() != null && detail.getImportPrice() != null) {
                    detail.setLineTotal(detail.getQuantity() * detail.getImportPrice());
                }

                // 2. Lấy Phân loại -> CHỈ CỘNG TỒN KHO (Giá nhập đã được lưu ở chính cái detail này rồi)
                if (detail.getVariant() != null && detail.getVariant().getVariantId() != null) {
                    ProductVariant variant = getVariantOrThrow(detail.getVariant().getVariantId());

                    int currentStock = getSafeStock(variant);
                    variant.setStockQuantity(currentStock + detail.getQuantity());

                    productVariantDAO.save(variant);
                    detail.setVariant(variant);
                }
            }
        }
        return importReceiptDAO.save(receipt);
    }

    @Override
    public List<ImportReceiptListDTO> getAllReceiptsForList() {
        return importReceiptDAO.findAll().stream()
                .map(this::mapToListDTO) // Gọi helper function cho gọn code
                .collect(Collectors.toList());
    }

    @Override
    public ImportReceiptDetailDTO getReceiptDetail(Integer id) {
        return importReceiptDAO.findById(id)
                .map(this::mapToDetailDTO) // Gọi helper function
                .orElse(null);
    }

    @Override
    @Transactional
    public ImportReceipt updateReceipt(Integer id, ImportReceipt receiptDetails) {
        return importReceiptDAO.findById(id).map(existing -> {
            existing.setNote(receiptDetails.getNote());
            return importReceiptDAO.save(existing);
        }).orElse(null);
    }

    @Override
    @Transactional
    public void cancelReceipt(Integer receiptId) {
        ImportReceipt receipt = importReceiptDAO.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập có ID: " + receiptId));

        if (receipt.getDetails() != null) {
            for (ImportReceiptDetail detail : receipt.getDetails()) {
                if (detail.getVariant() != null) {
                    ProductVariant variant = detail.getVariant();
                    int currentStock = getSafeStock(variant);

                    // Chống âm kho
                    if (currentStock < detail.getQuantity()) {
                        throw new RuntimeException(String.format(
                                "Lỗi: Phân loại '%s' chỉ còn %d cái, không đủ để hủy %d cái. Hàng đã xuất bán!",
                                getVariantFullName(variant), currentStock, detail.getQuantity()
                        ));
                    }
                    // Trừ kho hoàn trả
                    variant.setStockQuantity(currentStock - detail.getQuantity());
                    productVariantDAO.save(variant);
                }
            }
        }
        importReceiptDAO.delete(receipt);
    }

    // ==========================================
    //    CÁC HÀM HELPER DÙNG CHUNG (PRIVATE)
    // ==========================================

    private Product getProductOrThrow(Integer id) {
        if (id == null) throw new RuntimeException("ID Sản phẩm bị trống!");
        return productDAO.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));
    }

    private ProductVariant getVariantOrThrow(Integer id) {
        return productVariantDAO.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy phân loại ID: " + id));
    }

    private int getSafeStock(ProductVariant variant) {
        return variant.getStockQuantity() != null ? variant.getStockQuantity() : 0;
    }

    private String getVariantFullName(ProductVariant variant) {
        if (variant == null) return "Mặc định";
        String name = variant.getColorName() != null ? variant.getColorName() : "";
        if (variant.getOption2Value() != null && !variant.getOption2Value().isEmpty()) {
            name += " - " + variant.getOption2Value();
        }
        return name.isEmpty() ? "Mặc định" : name;
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "AD";
        String[] words = fullName.trim().split("\\s+");
        if (words.length == 1) return words[0].substring(0, 1).toUpperCase();
        return (words[0].substring(0, 1) + words[words.length - 1].substring(0, 1)).toUpperCase();
    }

    private String formatDate(Date date, String pattern) {
        if (date == null) return "";
        return new SimpleDateFormat(pattern).format(date);
    }

    // ==========================================
    //      CÁC HÀM ÁNH XẠ DTO (MAPPERS)
    // ==========================================

    private ImportReceiptListDTO mapToListDTO(ImportReceipt receipt) {
        ImportReceiptListDTO dto = new ImportReceiptListDTO();
        dto.setId(receipt.getReceiptId());
        dto.setCode(receipt.getReceiptCode());
        dto.setDate(formatDate(receipt.getReceiptDate(), "dd/MM/yyyy"));
        dto.setTotal(receipt.getTotalMoney());

        String empName = receipt.getEmployee() != null ? receipt.getEmployee().getFullName() : "Admin";
        dto.setCreatorName(empName);
        dto.setCreatorInitials(getInitials(empName));
        dto.setSupplier(receipt.getSupplier() != null ? receipt.getSupplier().getSupplierName() : "N/A");
        dto.setNote(receipt.getNote());
        return dto;
    }

    private ImportReceiptDetailDTO mapToDetailDTO(ImportReceipt receipt) {
        ImportReceiptDetailDTO dto = new ImportReceiptDetailDTO();
        dto.setReceiptCode(receipt.getReceiptCode());
        dto.setSupplierName(receipt.getSupplier() != null ? receipt.getSupplier().getSupplierName() : "N/A");
        dto.setReceiptDate(formatDate(receipt.getReceiptDate(), "dd/MM/yyyy HH:mm"));
        dto.setEmployeeName(receipt.getEmployee() != null ? receipt.getEmployee().getFullName() : "Admin");
        dto.setTotalValue(receipt.getTotalMoney());
        dto.setNote(receipt.getNote());

        List<ImportReceiptDetailDTO.ImportReceiptItemDTO> items = receipt.getDetails().stream()
                .map(this::mapToItemDTO)
                .collect(Collectors.toList());
        dto.setItems(items);
        return dto;
    }

    private ImportReceiptDetailDTO.ImportReceiptItemDTO mapToItemDTO(ImportReceiptDetail detail) {
        ImportReceiptDetailDTO.ImportReceiptItemDTO itemDTO = new ImportReceiptDetailDTO.ImportReceiptItemDTO();
        itemDTO.setName(detail.getProduct().getName());
        itemDTO.setQuantity(detail.getQuantity());
        itemDTO.setImportPrice(detail.getImportPrice());

        int qty = detail.getQuantity() != null ? detail.getQuantity() : 0;
        double price = detail.getImportPrice() != null ? detail.getImportPrice() : 0.0;
        itemDTO.setLineTotal(detail.getLineTotal() != null ? detail.getLineTotal() : (qty * price));

        if (detail.getVariant() != null) {
            ProductVariant v = detail.getVariant();
            itemDTO.setVariantName(getVariantFullName(v));
            itemDTO.setCode(v.getSkuCode() != null ? v.getSkuCode() : "SP-" + detail.getProduct().getProductId());

            String img = (v.getImageUrl() != null && !v.getImageUrl().isEmpty()) ? v.getImageUrl() : detail.getProduct().getImageUrl();
            itemDTO.setImage(img != null && !img.isEmpty() ? img : "https://via.placeholder.com/50");
        } else {
            itemDTO.setVariantName("Mặc định");
            itemDTO.setCode("SP-" + detail.getProduct().getProductId());
            String img = detail.getProduct().getImageUrl();
            itemDTO.setImage(img != null && !img.isEmpty() ? img : "https://via.placeholder.com/50");
        }
        return itemDTO;
    }
}