package com.poly.backend.repository;

import com.poly.backend.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    // Để trống thế này là Spring Boot tự động hiểu và cấp sẵn các hàm tìm kiếm (findById)
}