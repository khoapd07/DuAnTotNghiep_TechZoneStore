package com.poly.backend.service.impl;

import com.poly.backend.dao.ImportReceiptDAO;
import com.poly.backend.dao.SupplierDAO;
import com.poly.backend.dto.SupplierDTO;
import com.poly.backend.entity.Supplier;
import com.poly.backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierDAO supplierDAO;

    // Tiêm thêm DAO của Phiếu nhập để check điều kiện Xóa (TC208, TC209)
    @Autowired
    private ImportReceiptDAO importReceiptDAO;

    // ======================================================
    // 1. HÀM DTO (Lấy danh sách trả về cho Vue.js)
    // ======================================================
    private SupplierDTO mapToDTO(Supplier entity) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(entity.getSupplierId());
        dto.setName(entity.getSupplierName());
        dto.setContact(entity.getContactName());
        dto.setPhone(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setTaxCode(entity.getTaxCode());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierDAO.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ======================================================
    // 2. CÁC HÀM CRUD GỐC
    // ======================================================

    @Override
    public Supplier getSupplierById(Integer id) {
        return supplierDAO.findById(id).orElse(null);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        // Mặc định tạo mới là đang hợp tác
        if (supplier.getStatus() == null) {
            supplier.setStatus(true);
        }

        // Vue.js đã check trùng lặp rồi, backend chỉ việc lưu!
        return supplierDAO.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplierDetails) {
        Supplier existingSupplier = getSupplierById(id);
        if (existingSupplier != null) {

            // Vue.js đã check trùng lặp rồi, backend chỉ việc cập nhật!
            existingSupplier.setSupplierName(supplierDetails.getSupplierName());
            existingSupplier.setContactName(supplierDetails.getContactName());
            existingSupplier.setPhoneNumber(supplierDetails.getPhoneNumber());
            existingSupplier.setEmail(supplierDetails.getEmail());
            existingSupplier.setAddress(supplierDetails.getAddress());
            existingSupplier.setTaxCode(supplierDetails.getTaxCode());
            existingSupplier.setStatus(supplierDetails.getStatus());

            return supplierDAO.save(existingSupplier);
        }
        return null;
    }

    @Override
    public void deleteSupplier(Integer id) {
        Supplier existingSupplier = getSupplierById(id);
        if (existingSupplier != null) {

            // BẮT BUỘC GIỮ LẠI: Kiểm tra xem đã có Phiếu Nhập nào dùng NCC này chưa (TC209)
            boolean hasReceipts = importReceiptDAO.existsBySupplier_SupplierId(id);
            if (hasReceipts) {
                // Ném lỗi để từ chối Xóa
                throw new IllegalArgumentException("Không thể xóa NCC đã có dữ liệu phiếu nhập");
            }

            // XÓA HẲN: Xóa vĩnh viễn khỏi Database thay vì chỉ đổi trạng thái
            supplierDAO.deleteById(id);
        }
    }
}