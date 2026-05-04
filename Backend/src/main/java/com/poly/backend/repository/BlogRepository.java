package com.poly.backend.repository;

import com.poly.backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

    List<Blog> findByActiveTrueOrderByCreatedAtDesc();

}