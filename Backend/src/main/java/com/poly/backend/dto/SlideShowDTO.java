package com.poly.backend.dto;

import lombok.Data;

@Data
public class SlideShowDTO {
    private Integer slideId;
    private String imageUrl;
    private String badgeText;
    private String badgeClass;
    private String title;
    private String titleHighlight;
    private String highlightClass;
    private String description;
    private Integer displayOrder;
    private Boolean active;
}