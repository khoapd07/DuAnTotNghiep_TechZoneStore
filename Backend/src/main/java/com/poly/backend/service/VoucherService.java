package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Voucher;

public interface VoucherService {
    List<Voucher> findAll();
    Voucher findById(Integer id);
    Voucher save(Voucher voucher);
    void deleteById(Integer id);
}