package com.poly.backend.controller;

import com.poly.backend.dao.ReviewDAO;
import com.poly.backend.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewDAO reviewDAO;

    // ==================== ENDPOINT MỚI: LẤY ĐÁNH GIÁ THEO SẢN PHẨM ====================
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Integer productId) {
        List<Review> reviews = reviewDAO.findByProductId(productId); // Đảm bảo DAO có method này
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendReview(@RequestBody Review review) {
        try {
            Review savedReview = reviewDAO.save(review);
            return ResponseEntity.ok("Gửi đánh giá thành công! ID: " + savedReview.getReviewId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}