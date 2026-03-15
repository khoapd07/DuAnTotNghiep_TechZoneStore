package com.poly.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.EmployeeDAO;
import com.poly.backend.dao.RoleDAO;
import com.poly.backend.entity.Employee;
import com.poly.backend.entity.Role;
import com.poly.backend.dto.EmployeeDTO;
import com.poly.backend.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeDAO.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO findById(Integer id) {
        Employee emp = employeeDAO.findById(id).orElse(null);
        return emp != null ? mapToDTO(emp) : null;
    }

    @Override
    public EmployeeDTO save(EmployeeDTO dto) {
        Employee employee;

        if (dto.getUserId() != null) {
            employee = employeeDAO.findById(dto.getUserId()).orElse(null);
            if (employee == null) return null;
        } else {
            employee = new Employee();
            employee.setUsername(dto.getUsername());
            employee.setPassword(passwordEncoder.encode("123456")); // Pass mặc định
        }

        // Cập nhật Quyền (Role) dựa trên ID gửi từ Frontend
        if(dto.getRoleId() != null) {
            Role role = roleDAO.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Quyền không tồn tại"));
            employee.setRole(role);
        }

        // Cập nhật các trường khác
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        if(dto.getStatus() != null) employee.setStatus(dto.getStatus());
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setSalary(dto.getSalary());
        employee.setHireDate(dto.getHireDate());

        // Cột department có thể bỏ hoặc để trống vì đã dùng Role
        employee.setDepartment(dto.getRoleName());

        return mapToDTO(employeeDAO.save(employee));
    }

    @Override
    public String toggleStatus(Integer id) {
        Employee emp = employeeDAO.findById(id).orElse(null);
        if (emp == null) return null;
        emp.setStatus(!emp.getStatus());
        employeeDAO.save(emp);
        return emp.getStatus() ? "Đã mở khóa" : "Đã khóa";
    }

    @Override
    public void deleteById(Integer id) {
        employeeDAO.deleteById(id);
    }

    // HÀM MAP: Lấy thêm RoleId và RoleName từ Entity sang DTO
    private EmployeeDTO mapToDTO(Employee emp) {
        return EmployeeDTO.builder()
                .userId(emp.getUserId())
                .username(emp.getUsername())
                .fullName(emp.getFullName())
                .email(emp.getEmail())
                .phoneNumber(emp.getPhoneNumber())
                .status(emp.getStatus())
                .employeeCode(emp.getEmployeeCode())
                .hireDate(emp.getHireDate())
                .salary(emp.getSalary())
                // Lấy Role từ bảng Users/Roles
                .roleId(emp.getRole() != null ? emp.getRole().getRoleId() : null)
                .roleName(emp.getRole() != null ? emp.getRole().getRoleName() : "N/A")
                .build();
    }
}