package com.poly.backend.dao;

import com.poly.backend.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierDAO extends JpaRepository<Supplier, Integer> {
    List<Supplier> findBySupplierName(String supplierName);
    List<Supplier> findByPhoneNumber(String phoneNumber);
    List<Supplier> findByEmail(String email);
    List<Supplier> findByTaxCode(String taxCode);
}