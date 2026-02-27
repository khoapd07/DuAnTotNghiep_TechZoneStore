package com.poly.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.poly.backend.dao.CartDAO;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.dao.UserDAO;
import com.poly.backend.entity.Cart;
import com.poly.backend.entity.Product;
import com.poly.backend.entity.User;
import com.poly.backend.dto.CartDTO;
import com.poly.backend.service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    @Override
    public List<CartDTO> findByCustomerId(Integer customerId) {
        // 1. Lấy danh sách Entity từ DB
        List<Cart> entities = cartDAO.findByCustomerId(customerId);

        // 2. Ánh xạ (Map) sang CartDTO để hiển thị đầy đủ thông tin sản phẩm
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addItem(CartDTO cartDto) {
        // Tìm toàn bộ giỏ hàng của khách để kiểm tra trùng sản phẩm
        List<Cart> cartItems = cartDAO.findByCustomerId(cartDto.getUserId());

        for (Cart item : cartItems) {
            // Lấy ID sản phẩm trực tiếp từ Entity để tránh lỗi Lazy Loading
            if (item.getProduct().getProductId().equals(cartDto.getProductId())) {
                item.setQuantity(item.getQuantity() + cartDto.getQuantity());
                cartDAO.saveAndFlush(item);
                return;
            }
        }

        // Nếu chưa có sản phẩm trong giỏ, tạo mới
        User user = userDAO.findById(cartDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng ID: " + cartDto.getUserId()));

        Product product = productDAO.findById(cartDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + cartDto.getProductId()));

        Cart newCart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(cartDto.getQuantity())
                .build();

        cartDAO.saveAndFlush(newCart);
    }

    @Override
    @Transactional
    public void updateQuantity(CartDTO cartDto) {
        Cart cart = cartDAO.findAll().stream()
                .filter(c -> c.getUser().getUserId().equals(cartDto.getUserId()) &&
                        c.getProduct().getProductId().equals(cartDto.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm này trong giỏ hàng của user: " + cartDto.getUserId()));

        cart.setQuantity(cartDto.getQuantity());
        cartDAO.saveAndFlush(cart);
    }

    @Override
    @Transactional
    public void clearCartByCustomerId(Integer customerId) {
        cartDAO.deleteByCustomerId(customerId);
    }

    @Override
    public void deleteById(Integer id) {
        cartDAO.deleteById(id);
    }

    // --- HÀM TRỢ GIÚP CHUYỂN ĐỔI DỮ LIỆU ---
    private CartDTO convertToDTO(Cart entity) {
        Product product = entity.getProduct();
        BigDecimal price = product.getPrice();
        BigDecimal quantity = BigDecimal.valueOf(entity.getQuantity());

        return CartDTO.builder()
                .cartId(entity.getCartId())
                .quantity(entity.getQuantity())
                .productId(product.getProductId())
                .userId(entity.getUser().getUserId())

                // Sửa lại 2 dòng này cho khớp với Product.java của bạn
                .productName(product.getName())   // Trước là getProductName() bị lỗi
                .productImage(product.getImageUrl()) // Trước là getImage() bị lỗi

                .productPrice(price)
                .productSalePrice(product.getSalePrice()) // Thêm giá sale nếu bạn muốn hiển thị
                .totalPrice(price.multiply(quantity))
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}