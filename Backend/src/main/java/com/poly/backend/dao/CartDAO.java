package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Cart;

@Repository
public interface CartDAO extends JpaRepository<Cart, Integer> {
}