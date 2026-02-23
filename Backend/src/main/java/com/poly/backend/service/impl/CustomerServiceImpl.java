package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.CustomerDAO;
import com.poly.backend.entity.Customer;
import com.poly.backend.service.CustomerService;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    public Customer findById(Integer id) {
        return customerDAO.findById(id).orElse(null);
    }

    public Customer save(Customer customer) {
        return customerDAO.save(customer);
    }

    public void deleteById(Integer id) {
        customerDAO.deleteById(id);
    }
}