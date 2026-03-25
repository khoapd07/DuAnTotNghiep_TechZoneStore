package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")   // ⭐ QUAN TRỌNG
    private Integer blogId;

    private String title;

    private String summary;

    private String content;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "author_id")
    private Integer authorId;

    private Integer views;

    private Boolean active;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}