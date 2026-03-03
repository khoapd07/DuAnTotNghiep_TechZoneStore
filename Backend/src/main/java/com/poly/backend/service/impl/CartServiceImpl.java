package com.poly.backend.service.impl;

import com.poly.backend.dao.CartDAO;
import com.poly.backend.dao.CartItemDAO;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.dao.UserDAO;
import com.poly.backend.dto.CartItemRequestDTO;
import com.poly.backend.dto.CartItemResponseDTO;
import com.poly.backend.dto.CartResponseDTO;
import com.poly.backend.entity.Cart;
import com.poly.backend.entity.CartItem;
import com.poly.backend.entity.Product;
import com.poly.backend.entity.User;
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

                // Logic chọn giá: Có Sale thì lấy giá Sale, không thì lấy giá gốc
                BigDecimal applyPrice = (p.getSalePrice() != null && p.getSalePrice().compareTo(BigDecimal.ZERO) > 0)
                        ? p.getSalePrice() : p.getPrice();

                // Thành tiền = Giá * Số lượng
                BigDecimal subTotal = applyPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                cartTotal = cartTotal.add(subTotal);

                itemDTOs.add(CartItemResponseDTO.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(p.getProductId())
                        .productName(p.getName())
                        .imageUrl(p.getImageUrl())
                        .price(p.getPrice())
                        .salePrice(p.getSalePrice())
                        .quantity(item.getQuantity())
                        .subTotal(subTotal) // DTO tự lo hiển thị, UI không cần tính
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

    // ======================================================
    // 2. LOGIC NGHIỆP VỤ (THÊM, SỬA, XÓA, GỘP)
    // ======================================================

    // Lấy Entity Giỏ hàng (Tạo mới nếu User chưa có)
    private Cart getEntityCart(Integer userId) {
        return cartDAO.findByUser_UserId(userId).orElseGet(() -> {
            User user = userDAO.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy User"));
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

        // CHỐT CHẶN: Ép kiểm tra số lượng tồn kho
        if (request.getQuantity() > product.getStockQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Kho chỉ còn " + product.getStockQuantity() + " sản phẩm!#:" + product.getStockQuantity());
        }

        if (cart.getCartItems() == null) cart.setCartItems(new ArrayList<>());

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // NẾU ĐÃ CÓ -> THỰC HIỆN CỘNG DỒN
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();

            if (newQuantity > product.getStockQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kho chỉ còn " + product.getStockQuantity() + " sản phẩm!");
            }
            item.setQuantity(newQuantity);
        } else {
            // NẾU CHƯA CÓ -> TẠO MỚI
            if (request.getQuantity() > product.getStockQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kho chỉ còn " + product.getStockQuantity() + " sản phẩm!");
            }
            cart.getCartItems().add(CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build());
        }

        return mapToDTO(cartDAO.save(cart));
    }

    @Override
    @Transactional
    public CartResponseDTO updateItemQuantity(Integer userId, CartItemRequestDTO request) {
        Cart cart = getEntityCart(userId);
        Product product = productDAO.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));

        if (request.getQuantity() > product.getStockQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kho chỉ còn " + product.getStockQuantity() + " sản phẩm!");
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (request.getQuantity() <= 0) {
                cart.getCartItems().remove(item);
                cartItemDAO.delete(item);
            } else {
                item.setQuantity(request.getQuantity()); // Ghi đè tuyệt đối
            }
        }
        return mapToDTO(cartDAO.save(cart));
    }

    @Override
    @Transactional
    public CartResponseDTO mergeLocalCart(Integer userId, List<CartItemRequestDTO> localCartItems) {
        if (localCartItems != null && !localCartItems.isEmpty()) {
            for (CartItemRequestDTO localItem : localCartItems) {
                try {
                    // Vì hàm addOrUpdateCartItem giờ đã là CỘNG DỒN, nên code gộp giỏ hàng trở nên siêu ngắn!
                    addOrUpdateCartItem(userId, localItem);
                } catch (ResponseStatusException e) {
                    System.out.println("Bỏ qua món hàng gộp vượt tồn kho: " + e.getReason());
                }
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