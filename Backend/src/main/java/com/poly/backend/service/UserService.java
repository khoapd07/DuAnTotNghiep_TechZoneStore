package com.poly.backend.service;

import java.util.List;

import com.poly.backend.dto.ChangePasswordDTO;
import com.poly.backend.dto.UserProfileDTO;
import com.poly.backend.entity.User;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    User save(User user);
    void deleteById(Integer id);

    User findByUsername(String username);
    User updateProfile(String username, UserProfileDTO dto);
    void changePassword(String username, ChangePasswordDTO dto);
}