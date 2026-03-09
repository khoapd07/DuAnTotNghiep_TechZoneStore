package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.BrandDAO;
import com.poly.backend.entity.Brand;
import com.poly.backend.dto.BrandDTO;
import com.poly.backend.service.BrandService;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandDAO brandDAO;

    @Override
    public List<BrandDTO> findAllWithStats() {
        return brandDAO.findAllWithStats();
    }

    @Override
    public Brand findById(Integer id) {
        return brandDAO.findById(id).orElse(null);
    }

    @Override
    public Brand save(Brand brand) {
        return brandDAO.save(brand);
    }

    @Override
    public void deleteById(Integer id) {
        brandDAO.deleteById(id);
    }
}