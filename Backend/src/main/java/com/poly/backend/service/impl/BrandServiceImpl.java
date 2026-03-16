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

    // MỚI THÊM: Tạo mới có Validate
    @Override
    public Brand create(Brand brand) {
        if (brandDAO.existsByBrandName(brand.getBrandName())) {
            throw new IllegalArgumentException("Tên thương hiệu này đã tồn tại!");
        }
        return brandDAO.save(brand);
    }

    // MỚI THÊM: Cập nhật có Validate
    @Override
    public Brand update(Integer id, Brand brand) {
        if (brandDAO.existsByBrandNameAndBrandIdNot(brand.getBrandName(), id)) {
            throw new IllegalArgumentException("Tên thương hiệu này đã bị trùng với thương hiệu khác!");
        }

        Brand existingBrand = brandDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thương hiệu!"));

        existingBrand.setBrandName(brand.getBrandName());
        return brandDAO.save(existingBrand);
    }

    @Override
    public void deleteById(Integer id) {
        brandDAO.deleteById(id);
    }
}