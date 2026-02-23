package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Employee;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Integer> {
}