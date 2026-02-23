package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Customer;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(Integer id);
    Customer save(Customer customer);
    void deleteById(Integer id);
}