package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.CartDAO;
import com.poly.backend.entity.Cart;
import com.poly.backend.service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;

    public List<Cart> findAll() {
        return cartDAO.findAll();
    }

    public Cart findById(Integer id) {
        return cartDAO.findById(id).orElse(null);
    }

    public Cart save(Cart cart) {
        return cartDAO.save(cart);
    }

    public void deleteById(Integer id) {
        cartDAO.deleteById(id);
    }
}