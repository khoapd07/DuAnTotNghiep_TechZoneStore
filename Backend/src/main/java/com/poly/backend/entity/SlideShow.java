package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "SlideShows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideShow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slide_id")
    private Integer slideId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "badge_text")
    private String badgeText;

    @Column(name = "badge_class")
    private String badgeClass;

    private String title;

    @Column(name = "title_highlight")
    private String titleHighlight;

    @Column(name = "highlight_class")
    private String highlightClass;

    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;

    private Boolean active = true;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
}