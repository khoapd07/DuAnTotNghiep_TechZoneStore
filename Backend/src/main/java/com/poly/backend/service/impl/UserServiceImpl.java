package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.UserDAO;
import com.poly.backend.entity.User;
import com.poly.backend.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findById(Integer id) {
        return userDAO.findById(id).orElse(null);
    }

    public User save(User user) {
        return userDAO.save(user);
    }

    public void deleteById(Integer id) {
        userDAO.deleteById(id);
    }
}