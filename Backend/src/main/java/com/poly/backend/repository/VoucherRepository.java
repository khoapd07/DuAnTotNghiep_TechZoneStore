package com.poly.backend.repository;

import com.poly.backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    
    Optional<Voucher> findByCode(String code);
}