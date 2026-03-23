package com.poly.backend.service;

import com.poly.backend.dto.SupplierDTO;
import com.poly.backend.entity.Supplier;
import java.util.List;

public interface SupplierService {
    // Hàm này đã được đổi sang trả về DTO để khớp với Vue.js
    List<SupplierDTO> getAllSuppliers();

    // Các hàm quản lý giữ nguyên Entity
    Supplier getSupplierById(Integer id);
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Integer id, Supplier supplier);
    void deleteSupplier(Integer id);
}