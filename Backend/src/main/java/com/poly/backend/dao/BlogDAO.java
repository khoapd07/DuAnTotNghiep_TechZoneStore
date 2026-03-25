package com.poly.backend.dao;

import com.poly.backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogDAO extends JpaRepository<Blog, Integer> {

    List<Blog> findByActiveTrueOrderByCreatedAtDesc();

}