package com.poly.backend.controller;

import com.poly.backend.dao.ReviewDAO;
import com.poly.backend.dto.ReviewDTO;
import com.poly.backend.entity.Review;
import com.poly.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReviewController {

    private final ReviewDAO reviewDAO;
    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    // 2. LẤY ĐÁNH GIÁ THEO USER (Trang OrderVote)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    // 3. GỬI ĐÁNH GIÁ MỚI
    @PostMapping("/send")
    public ResponseEntity<?> sendReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO savedReview = reviewService.createReview(reviewDTO);
            return ResponseEntity.ok(savedReview);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}