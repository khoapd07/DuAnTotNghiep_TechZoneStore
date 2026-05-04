package com.poly.backend.service.impl;

import com.poly.backend.repository.BlogRepository;
import com.poly.backend.dto.BlogDTO;
import com.poly.backend.entity.Blog;
import com.poly.backend.service.BlogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    public BlogServiceImpl(BlogRepository blogDAO) {
        this.blogRepository = blogDAO;
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BlogDTO getBlogById(Integer id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        return convertToDTO(blog);
    }

    @Override
    public BlogDTO createBlog(BlogDTO dto) {
        Blog blog = new Blog();
        blog.setTitle(dto.getTitle());
        blog.setSummary(dto.getSummary());
        blog.setContent(dto.getContent());
        blog.setThumbnailUrl(dto.getThumbnailUrl());
        blog.setAuthorId(dto.getAuthorId());
        blog.setViews(dto.getViews() != null ? dto.getViews() : 0);
        blog.setActive(dto.getActive() != null ? dto.getActive() : true);

        Date now = new Date();
        blog.setCreatedAt(now);
        blog.setUpdatedAt(now);

        Blog saved = blogRepository.save(blog);
        return convertToDTO(saved);
    }

    @Override
    public BlogDTO updateBlog(Integer id, BlogDTO dto) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));

        blog.setTitle(dto.getTitle());
        blog.setSummary(dto.getSummary());
        blog.setContent(dto.getContent());
        blog.setThumbnailUrl(dto.getThumbnailUrl());

        if (dto.getAuthorId() != null) blog.setAuthorId(dto.getAuthorId());
        if (dto.getViews() != null) blog.setViews(dto.getViews());
        if (dto.getActive() != null) blog.setActive(dto.getActive());

        blog.setUpdatedAt(new Date());

        Blog saved = blogRepository.save(blog);
        return convertToDTO(saved);
    }

    @Override
    public void deleteBlog(Integer id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        blogRepository.delete(blog);
    }

    public List<BlogDTO> getActiveBlogs() {
        return blogRepository.findAll().stream()
                .filter(b -> Boolean.TRUE.equals(b.getActive()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BlogDTO convertToDTO(Blog blog) {
        BlogDTO dto = new BlogDTO();
        dto.setBlogId(blog.getBlogId());
        dto.setTitle(blog.getTitle());
        dto.setSummary(blog.getSummary());
        dto.setContent(blog.getContent());
        dto.setThumbnailUrl(blog.getThumbnailUrl());
        dto.setAuthorId(blog.getAuthorId());
        dto.setViews(blog.getViews());
        dto.setActive(blog.getActive());
        dto.setCreatedAt(blog.getCreatedAt());
        dto.setUpdatedAt(blog.getUpdatedAt());
        return dto;
    }
}