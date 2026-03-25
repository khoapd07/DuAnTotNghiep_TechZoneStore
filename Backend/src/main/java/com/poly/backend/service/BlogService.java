package com.poly.backend.service;

import com.poly.backend.dto.BlogDTO;

import java.util.List;

public interface BlogService {

    List<BlogDTO> getAllBlogs();

    BlogDTO getBlogById(Integer id);
    BlogDTO createBlog(BlogDTO dto);
    BlogDTO updateBlog(Integer id, BlogDTO dto);
    void deleteBlog(Integer id);


}