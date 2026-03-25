package com.poly.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BlogDTO {

    private Integer blogId;
    private String title;
    private String summary;
    private String content;
    private String thumbnailUrl;

    private Integer authorId;
    private Integer views;
    private Boolean active;

    private Date createdAt;
    private Date updatedAt;
}