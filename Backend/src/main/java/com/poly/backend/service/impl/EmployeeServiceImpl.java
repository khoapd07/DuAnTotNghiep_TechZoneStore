package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.EmployeeDAO;
import com.poly.backend.entity.Employee;
import com.poly.backend.service.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;

    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    public Employee findById(Integer id) {
        return employeeDAO.findById(id).orElse(null);
    }

    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    public void deleteById(Integer id) {
        employeeDAO.deleteById(id);
    }
}