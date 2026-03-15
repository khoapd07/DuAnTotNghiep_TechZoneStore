package com.poly.backend.service;

import java.util.List;
import com.poly.backend.dto.EmployeeDTO;

public interface EmployeeService {
    List<EmployeeDTO> findAll();
    EmployeeDTO findById(Integer id);
    EmployeeDTO save(EmployeeDTO dto);
    void deleteById(Integer id);
    String toggleStatus(Integer id);
}