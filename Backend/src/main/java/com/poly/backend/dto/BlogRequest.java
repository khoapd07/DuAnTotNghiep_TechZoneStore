package com.poly.backend.dto;

import lombok.Data;

@Data
public class BlogRequest {
    private String title;
    private String summary;
    private String content;
    private String thumbnailUrl;
    private Integer authorId; // Frontend sẽ gửi ID của admin/staff đang đăng nhập
    private Boolean active;
}