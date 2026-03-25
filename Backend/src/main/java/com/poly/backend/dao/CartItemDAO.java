package com.poly.backend.dao;

import com.poly.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemDAO extends JpaRepository<CartItem, Integer> {
}