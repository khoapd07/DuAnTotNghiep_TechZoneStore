package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Brand;
import com.poly.backend.dto.BrandDTO;

public interface BrandService {
    List<BrandDTO> findAllWithStats();
    Brand findById(Integer id);

    // MỚI THÊM: Tách hàm save thành create và update
    Brand create(Brand brand);
    Brand update(Integer id, Brand brand);

    void deleteById(Integer id);
}