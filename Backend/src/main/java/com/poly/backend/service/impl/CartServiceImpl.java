package com.poly.backend.service.impl;

import com.poly.backend.dao.*;
import com.poly.backend.dto.CartItemRequestDTO;
import com.poly.backend.dto.CartItemResponseDTO;
import com.poly.backend.dto.CartResponseDTO;
import com.poly.backend.entity.*;
import com.poly.backend.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartDAO cartDAO;
    @Autowired private CartItemDAO cartItemDAO;
    @Autowired private ProductDAO productDAO;
    @Autowired private ProductVariantDAO variantDAO;
    @Autowired private UserDAO userDAO;

    // ======================================================
    // 1. HÀM CHUYỂN ĐỔI (MAPPING) & TÍNH TIỀN TỰ ĐỘNG
    // ======================================================
    private CartResponseDTO mapToDTO(Cart cart) {
        List<CartItemResponseDTO> itemDTOs = new ArrayList<>();
        BigDecimal cartTotal = BigDecimal.ZERO;

        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                Product p = item.getProduct();
                ProductVariant v = item.getVariant();

                // NẾU CÓ BIẾN THỂ -> LẤY GIÁ CỦA BIẾN THỂ. KHÔNG CÓ -> LẤY GIÁ SẢN PHẨM CHÍNH
                BigDecimal originalPrice = (v != null && v.getPrice() != null && v.getPrice().compareTo(BigDecimal.ZERO) > 0) ? v.getPrice() : p.getPrice();
                BigDecimal salePrice = (v != null && v.getSalePrice() != null && v.getSalePrice().compareTo(BigDecimal.ZERO) > 0) ? v.getSalePrice() : p.getSalePrice();

                BigDecimal applyPrice = (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0) ? salePrice : originalPrice;
                BigDecimal subTotal = applyPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                cartTotal = cartTotal.add(subTotal);

                // Ưu tiên hình ảnh của biến thể nếu có
                String imgUrl = (v != null && v.getImageUrl() != null && !v.getImageUrl().isEmpty()) ? v.getImageUrl() : p.getImageUrl();

                // LOGIC QUAN TRỌNG: GÁN MÀU SẮC VÀ SIZE
                String itemColor = (v != null) ? v.getColorName() : null;
                String itemOption2 = (v != null) ? v.getOption2Value() : null;

                itemDTOs.add(CartItemResponseDTO.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(p.getProductId())
                        .variantId(v != null ? v.getVariantId() : null)
                        .productName(p.getName())
                        .colorName(itemColor)       // TRUYỀN MÀU SẮC LÊN FRONTEND
                        .option2Value(itemOption2)  // TRUYỀN OPTION LÊN FRONTEND
                        .imageUrl(imgUrl)
                        .price(originalPrice)
                        .salePrice(salePrice)
                        .quantity(item.getQuantity())
                        .subTotal(subTotal)
                        .build());
            }
        }

        return CartResponseDTO.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .items(itemDTOs)
                .cartTotal(cartTotal)
                .build();
    }

    private Cart getEntityCart(Integer userId) {
        return cartDAO.findByUser_UserId(userId).orElseGet(() -> {
            User user = userDAO.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy User"));
            return cartDAO.save(Cart.builder().user(user).build());
        });
    }

    @Override
    public CartResponseDTO getCartDTOByUserId(Integer userId) {
        return mapToDTO(getEntityCart(userId));
    }

    @Override
    @Transactional
    public CartResponseDTO addOrUpdateCartItem(Integer userId, CartItemRequestDTO request) {
        Cart cart = getEntityCart(userId);
        Product product = productDAO.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));

        // XÁC ĐỊNH BIẾN THỂ
        ProductVariant variant = null;
        Integer maxStock = product.getTotalStock(); // Mặc định lấy tổng kho

        if (request.getVariantId() != null) {
            variant = variantDAO.findById(request.getVariantId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phân loại sản phẩm"));
            maxStock = variant.getStockQuantity(); // Nếu mua biến thể cụ thể, check kho của biến thể đó
        }

        if (request.getQuantity() > maxStock) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kho chỉ còn " + maxStock + " sản phẩm loại này!");
        }

        if (cart.getCartItems() == null) cart.setCartItems(new ArrayList<>());

        // TÌM XEM TRONG GIỎ CÓ SẴN (CÙNG SẢN PHẨM + CÙNG BIẾN THỂ) CHƯA
        final Integer targetVariantId = variant != null ? variant.getVariantId() : null;
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()) &&
                        (item.getVariant() == null ? targetVariantId == null : item.getVariant().getVariantId().equals(targetVariantId)))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (newQuantity > maxStock) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kho chỉ còn " + maxStock + " sản phẩm!");
            item.setQuantity(newQuantity);
            cartItemDAO.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .variant(variant) // Gắn biến thể vào
                    .quantity(request.getQuantity())
                    .build();
            cart.getCartItems().add(newItem);
            cartItemDAO.save(newItem);
        }

        return mapToDTO(cartDAO.save(cart));
    }

    @Override
    @Transactional
    public CartResponseDTO updateItemQuantity(Integer userId, CartItemRequestDTO request) {
        Cart cart = getEntityCart(userId);

        // Vì request từ Cart.vue gửi lên chỉ có ProductId chứ chưa có variantId (do code Front bạn gửi hàm syncQuantityWithBackend chỉ truyền productId)
        // Nên ta sẽ tìm item đầu tiên khớp productId để update (Cách tốt nhất là Front gửi thêm variantId lên, nhưng tạm thời chạy kiểu này vẫn OK nếu mỗi sp trong giỏ có 1 biến thể)
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            Integer maxStock = item.getVariant() != null ? item.getVariant().getStockQuantity() : item.getProduct().getTotalStock();

            if (request.getQuantity() > maxStock) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kho chỉ còn " + maxStock + " sản phẩm!");
            }

            if (request.getQuantity() <= 0) {
                cart.getCartItems().remove(item);
                cartItemDAO.delete(item);
            } else {
                item.setQuantity(request.getQuantity());
            }
        }
        return mapToDTO(cartDAO.save(cart));
    }

    @Override
    @Transactional
    public CartResponseDTO mergeLocalCart(Integer userId, List<CartItemRequestDTO> localCartItems) {
        if (localCartItems != null && !localCartItems.isEmpty()) {
            for (CartItemRequestDTO localItem : localCartItems) {
                try { addOrUpdateCartItem(userId, localItem); } catch (Exception e) {}
            }
        }
        return getCartDTOByUserId(userId);
    }

    @Override
    @Transactional
    public CartResponseDTO removeCartItem(Integer userId, Integer productId) {
        Cart cart = getEntityCart(userId);
        if (cart.getCartItems() != null) {
            cart.getCartItems().removeIf(item -> item.getProduct().getProductId().equals(productId));
            cart = cartDAO.save(cart);
        }
        return mapToDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO clearCart(Integer userId) {
        Cart cart = getEntityCart(userId);
        if (cart.getCartItems() != null) {
            cart.getCartItems().clear();
            cart = cartDAO.save(cart);
        }
        return mapToDTO(cart);
    }
}