package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Role;

public interface RoleService {
    List<Role> findAll();
    Role findById(Integer id);
    Role save(Role role);
    void deleteById(Integer id);
}