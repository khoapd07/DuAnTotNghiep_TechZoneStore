package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.RoleDAO;
import com.poly.backend.entity.Role;
import com.poly.backend.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public Role findById(Integer id) {
        return roleDAO.findById(id).orElse(null);
    }

    public Role save(Role role) {
        return roleDAO.save(role);
    }

    public void deleteById(Integer id) {
        roleDAO.deleteById(id);
    }
}