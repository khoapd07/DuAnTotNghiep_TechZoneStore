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
import com.poly.backend.dao.CategoryDAO;
import com.poly.backend.dao.BrandDAO;
import com.poly.backend.entity.Product;
import com.poly.backend.entity.Category;
import com.poly.backend.entity.Brand;
import com.poly.backend.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final BrandDAO brandDAO;

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productDAO.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productDAO.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        productDAO.deleteById(id);
    }

    @Override
    public Page<ProductDTO> getProducts(String keyword, Integer categoryId, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productDAO.searchAndFilterProducts(keyword, categoryId, brandId, minPrice, maxPrice, pageable);
        return productPage.map(this::mapToDTO);
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        Product product = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
        return mapToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        validateSalePrice(dto); // Kiểm tra logic Giá Khuyến Mãi
        Product product = new Product();
        mapToEntity(dto, product);

        Product savedProduct = productDAO.save(product);
        return mapToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO dto) {
        validateSalePrice(dto); // Kiểm tra logic Giá Khuyến Mãi
        Product existingProduct = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        mapToEntity(dto, existingProduct);

        Product updatedProduct = productDAO.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    // ==================== 2 HÀM MỚI THEO YÊU CẦU ====================
    // Lấy danh sách sản phẩm CÓ GIẢM GIÁ
    public List<ProductDTO> getDiscountedProducts() {
        return productDAO.findAll().stream()
                .filter(p -> p.getSalePrice() != null && p.getSalePrice().compareTo(BigDecimal.ZERO) > 0)
                .map(this::mapToDTO)
                .toList();
    }

    // Lấy 8 sản phẩm MỚI NHẤT (dựa vào createdAt)
    public List<ProductDTO> getTop8NewestProducts() {
        Pageable top8 = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt"));
        return productDAO.findAll(top8).getContent().stream()
                .map(this::mapToDTO)
                .toList();
    }
    // ================================================================

    // Logic kiểm tra giá khuyến mãi hợp lệ
    private void validateSalePrice(ProductDTO dto) {
        if (dto.getSalePrice() != null && dto.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
            if (dto.getSalePrice().compareTo(dto.getPrice()) >= 0) {
                throw new IllegalArgumentException("Lỗi: Giá khuyến mãi phải nhỏ hơn giá bán gốc!");
            }
        } else {
            dto.setSalePrice(null);
        }
    }

    // Hàm 1: Entity -> DTO
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
                .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getBrandId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getBrandName() : null)
                .build();
    }

    // Hàm 2: DTO -> Entity
    private void mapToEntity(ProductDTO dto, Product product) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setActive(dto.getActive() != null ? dto.getActive() : true);

        if (dto.getCategoryId() != null) {
            Category category = categoryDAO.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
            product.setCategory(category);
        }

        if (dto.getBrandId() != null) {
            Brand brand = brandDAO.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));
            product.setBrand(brand);
        }
    }
}