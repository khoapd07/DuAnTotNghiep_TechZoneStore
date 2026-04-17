package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.VoucherRepository;
import com.poly.backend.entity.Voucher;
import com.poly.backend.service.VoucherService;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    public Voucher findById(Integer id) {
        return voucherRepository.findById(id).orElse(null);
    }

    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public void deleteById(Integer id) {
        voucherRepository.deleteById(id);
    }

    // MỚI THÊM
    @Override
    public Voucher findByCode(String code) {
        return voucherRepository.findByCode(code).orElse(null);
    }
}