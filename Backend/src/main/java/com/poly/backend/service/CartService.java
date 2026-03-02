package com.poly.backend.service;

import com.poly.backend.dto.CartItemRequestDTO;
import com.poly.backend.dto.CartResponseDTO;
import java.util.List;

public interface CartService {

    // Đã đổi tên hàm và kiểu trả về thành DTO
    CartResponseDTO getCartDTOByUserId(Integer userId);

    CartResponseDTO addOrUpdateCartItem(Integer userId, CartItemRequestDTO request);

    CartResponseDTO mergeLocalCart(Integer userId, List<CartItemRequestDTO> localCartItems);

    CartResponseDTO removeCartItem(Integer userId, Integer productId);

    CartResponseDTO clearCart(Integer userId);
}