package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Customer;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer> {
}