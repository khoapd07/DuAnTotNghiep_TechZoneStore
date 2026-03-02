package com.poly.backend.dao;
import com.poly.backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoucherDAO extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByCode(String code);
}