package com.poly.backend.controller;

import com.poly.backend.dto.BlogDTO;
import com.poly.backend.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin("*")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // Public: danh sách blog
    @GetMapping
    public List<BlogDTO> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    // Public: chi tiết blog (anh đang có rồi luôn nè), dùng cái này lấy được chi tiết bài đăng
    @GetMapping("/{id}")
    public BlogDTO getBlogDetail(@PathVariable Integer id) {
        return blogService.getBlogById(id);
    }

    // Admin: tạo blog
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BlogDTO createBlog(@Valid @RequestBody BlogDTO dto) {
        return blogService.createBlog(dto);
    }

    // Admin: cập nhật blog
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogDTO updateBlog(@PathVariable Integer id, @Valid @RequestBody BlogDTO dto) {
        return blogService.updateBlog(id, dto);
    }

    // Admin: xóa blog
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBlog(@PathVariable Integer id) {
        blogService.deleteBlog(id);
    }
}