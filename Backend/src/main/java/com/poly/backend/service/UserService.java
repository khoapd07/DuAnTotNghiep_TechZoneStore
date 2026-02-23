package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.User;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    User save(User user);
    void deleteById(Integer id);
}