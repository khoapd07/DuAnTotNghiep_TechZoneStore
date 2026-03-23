package com.poly.backend.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.poly.backend.dto.ProductDTO;
import com.poly.backend.entity.ProductVariant;
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
        if (productDAO.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Tên sản phẩm này đã tồn tại!");
        }

        validateSalePrice(dto);
        Product product = new Product();
        mapToEntity(dto, product);

        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
            List<ProductVariant> variants = new ArrayList<>();
            for (ProductVariant v : dto.getVariants()) {
                v.setProduct(product);
                if (v.getStockQuantity() == null) v.setStockQuantity(0);

                // FIX LỖI: Tự động sinh mã SKU nếu rỗng để tránh vỡ SQL (Unique constraint)
                if (v.getSkuCode() == null || v.getSkuCode().trim().isEmpty()) {
                    v.setSkuCode("SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                }

                variants.add(v);
            }
            product.setVariants(variants);
        }

        Product savedProduct = productDAO.save(product);
        return mapToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO dto) {
        if (productDAO.existsByNameAndProductIdNot(dto.getName(), id)) {
            throw new IllegalArgumentException("Tên sản phẩm này đã bị trùng với một sản phẩm khác!");
        }

        validateSalePrice(dto);
        Product existingProduct = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        mapToEntity(dto, existingProduct);

        // FIX LỖI: Thuật toán cập nhật collection an toàn cho Hibernate
        if (dto.getVariants() != null) {

            // 1. Lấy danh sách ID các variant được truyền xuống (những cái được giữ lại)
            List<Integer> newVariantIds = dto.getVariants().stream()
                    .map(ProductVariant::getVariantId)
                    .filter(vId -> vId != null)
                    .collect(Collectors.toList());

            // 2. Xóa các variant cũ đã bị xóa khỏi giao diện
            existingProduct.getVariants().removeIf(v -> !newVariantIds.contains(v.getVariantId()));

            // 3. Duyệt qua mảng mới để Update hoặc Insert thêm
            for (ProductVariant newV : dto.getVariants()) {
                if (newV.getVariantId() != null) {
                    // Cập nhật biến thể có sẵn
                    existingProduct.getVariants().stream()
                            .filter(v -> v.getVariantId().equals(newV.getVariantId()))
                            .findFirst()
                            .ifPresent(existingV -> {
                                existingV.setColorName(newV.getColorName());
                                existingV.setOption2Value(newV.getOption2Value());
                                existingV.setImageUrl(newV.getImageUrl());
                                existingV.setPrice(newV.getPrice());
                                existingV.setSalePrice(newV.getSalePrice());
                                // TUYỆT ĐỐI KHÔNG CẬP NHẬT TỒN KHO ĐỂ BẢO TOÀN DỮ LIỆU
                            });
                } else {
                    // Thêm biến thể mới hoàn toàn (do bấm nút Tạo Tổ Hợp)
                    newV.setProduct(existingProduct);
                    if (newV.getStockQuantity() == null) newV.setStockQuantity(0);

                    // Tự động sinh mã SKU cho dòng mới
                    newV.setSkuCode("SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

                    existingProduct.getVariants().add(newV);
                }
            }
        } else {
            existingProduct.getVariants().clear();
        }

        Product updatedProduct = productDAO.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    public List<ProductDTO> getDiscountedProducts() {
        return productDAO.findAll().stream()
                .filter(p -> p.getSalePrice() != null && p.getSalePrice().compareTo(BigDecimal.ZERO) > 0)
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProductDTO> getTop8NewestProducts() {
        Pageable top8 = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt"));
        return productDAO.findAll(top8).getContent().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private void validateSalePrice(ProductDTO dto) {
        if (dto.getSalePrice() != null && dto.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
            if (dto.getSalePrice().compareTo(dto.getPrice()) >= 0) {
                throw new IllegalArgumentException("Giá khuyến mãi phải nhỏ hơn giá gốc!");
            }
        } else {
            dto.setSalePrice(null);
        }
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .stockQuantity(product.getTotalStock())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getBrandId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getBrandName() : null)
                .variants(product.getVariants())
                .attributes(product.getAttributes())
                .build();
    }

    private void mapToEntity(ProductDTO dto, Product product) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setActive(dto.getActive() != null ? dto.getActive() : true);
        product.setAttributes(dto.getAttributes());

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

    @Override
    public long getTotalProductsCount() {
        return productDAO.count();
    }

    @Override
    public long getLowStockProductsCount() {
        return productDAO.countByTotalStockLessThan(16);
    }

    @Override
    public Long getTotalStockQuantity() {
        Long total = productDAO.sumTotalStockQuantity();
        return total != null ? total : 0L;
    }

    @Override
    public List<ProductDTO> getFeaturedProducts() {
        Pageable top2 = PageRequest.of(0, 2);
        List<Product> featured = productDAO.findBestSellingProducts(top2);
        if (featured == null || featured.isEmpty()) {
            featured = productDAO.findTop2ByOrderByPriceDesc();
        }
        return featured.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Page<ProductDTO> getProducts(String keyword, Integer categoryId, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice, Boolean isSale, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productDAO.searchAndFilterProducts(keyword, categoryId, brandId, minPrice, maxPrice, isSale, pageable);
        return productPage.map(this::mapToDTO);
    }
}