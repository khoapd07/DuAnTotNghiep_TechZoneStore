package com.poly.backend.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.poly.backend.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.entity.Product;
import com.poly.backend.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public List<Product> findAll() {
        return productDAO.findAll();
    }

    public Product findById(Integer id) {
        return productDAO.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productDAO.save(product);
    }

    public void deleteById(Integer id) {
        productDAO.deleteById(id);
    }

    @Override
    public Page<ProductDTO> getProducts(String keyword, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, String sortDir) {
        // Xử lý hướng sắp xếp
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Gọi DAO lấy Page<Entity>
        Page<Product> productPage = productDAO.searchAndFilterProducts(keyword, minPrice, maxPrice, pageable);

        // Map Page<Entity> sang Page<DTO>
        return productPage.map(this::mapToDTO);
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        Product product = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id)); // Hoặc custom Exception của bạn
        return mapToDTO(product);
    }

    // Hàm tiện ích: Chuyển Entity sang DTO
    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                // Ánh xạ khóa ngoại an toàn (Kiểm tra null)
                // Lưu ý: Đổi .getCategoryName() / .getBrandName() cho khớp với tên hàm GETTER trong Entity Category/Brand của bạn nhé
                .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getBrandId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getBrandName() : null)
                .build();
    }
}