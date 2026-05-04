package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.repository.BrandRepository;
import com.poly.backend.entity.Brand;
import com.poly.backend.dto.BrandDTO;
import com.poly.backend.service.BrandService;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public List<BrandDTO> findAllWithStats() {
        return brandRepository.findAllWithStats();
    }

    @Override
    public Brand findById(Integer id) {
        return brandRepository.findById(id).orElse(null);
    }

    // MỚI THÊM: Tạo mới có Validate
    @Override
    public Brand create(Brand brand) {
        if (brandRepository.existsByBrandName(brand.getBrandName())) {
            throw new IllegalArgumentException("Tên thương hiệu này đã tồn tại!");
        }
        return brandRepository.save(brand);
    }

    // MỚI THÊM: Cập nhật có Validate
    @Override
    public Brand update(Integer id, Brand brand) {
        if (brandRepository.existsByBrandNameAndBrandIdNot(brand.getBrandName(), id)) {
            throw new IllegalArgumentException("Tên thương hiệu này đã bị trùng với thương hiệu khác!");
        }

        Brand existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thương hiệu!"));

        existingBrand.setBrandName(brand.getBrandName());
        return brandRepository.save(existingBrand);
    }

    @Override
    public void deleteById(Integer id) {
        brandRepository.deleteById(id);
    }
}