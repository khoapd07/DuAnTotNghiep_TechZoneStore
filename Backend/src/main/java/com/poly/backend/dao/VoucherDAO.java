package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Voucher;

@Repository
public interface VoucherDAO extends JpaRepository<Voucher, Integer> {
}