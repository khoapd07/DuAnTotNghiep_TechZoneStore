package com.poly.backend.dao;

import com.poly.backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoucherDAO extends JpaRepository<Voucher, Integer> {
    
    Optional<Voucher> findByCode(String code);
}