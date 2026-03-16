package com.poly.backend.service;

import java.util.List;

import com.poly.backend.dto.CustomerDTO;
import com.poly.backend.entity.Customer;

public interface CustomerService {
    List<CustomerDTO> findAll();
    CustomerDTO findById(Integer id);
    CustomerDTO save(CustomerDTO dto); // Nhận DTO, lưu xuống DB và trả về DTO
    void deleteById(Integer id);

    String toggleStatus(Integer id);
    CustomerDTO create(CustomerDTO dto);
}