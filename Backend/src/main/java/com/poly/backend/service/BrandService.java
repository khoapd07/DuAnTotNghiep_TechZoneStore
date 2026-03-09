package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Brand;
import com.poly.backend.dto.BrandDTO;

public interface BrandService {
    // Đổi sang trả về BrandDTO
    List<BrandDTO> findAllWithStats();

    Brand findById(Integer id);
    Brand save(Brand brand);
    void deleteById(Integer id);
}