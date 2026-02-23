package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Cart;

public interface CartService {

    List<Cart> findAll();

    Cart findById(Integer id);

    Cart save(Cart cart);

    void deleteById(Integer id);
}