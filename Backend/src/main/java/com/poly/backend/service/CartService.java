package com.poly.backend.service;

import com.poly.backend.dto.CartDTO;
import com.poly.backend.entity.Cart;
import java.util.List;

public interface CartService {
    List<CartDTO> findByCustomerId(Integer customerId);
    void clearCartByCustomerId(Integer customerId);
    void addItem(CartDTO cartDto);
    void updateQuantity(CartDTO cartDto);
    void deleteById(Integer cartId);

}