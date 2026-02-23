package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Employee;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(Integer id);
    Employee save(Employee employee);
    void deleteById(Integer id);
}