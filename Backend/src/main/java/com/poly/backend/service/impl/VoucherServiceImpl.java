package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.VoucherDAO;
import com.poly.backend.entity.Voucher;
import com.poly.backend.service.VoucherService;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherDAO voucherDAO;

    public List<Voucher> findAll() {
        return voucherDAO.findAll();
    }

    public Voucher findById(Integer id) {
        return voucherDAO.findById(id).orElse(null);
    }

    public Voucher save(Voucher voucher) {
        return voucherDAO.save(voucher);
    }

    public void deleteById(Integer id) {
        voucherDAO.deleteById(id);
    }
}