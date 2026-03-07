package com.poly.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.poly.backend.dao.ProductDAO;
import com.poly.backend.dao.UserDAO;
import com.poly.backend.dto.ReviewDTO;
import com.poly.backend.entity.Product;
import com.poly.backend.entity.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.ReviewDAO;
import com.poly.backend.entity.Review;
import com.poly.backend.service.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    public List<Review> findAll() {
        return reviewDAO.findAll();
    }

    public Review findById(Integer id) {
        return reviewDAO.findById(id).orElse(null);
    }

    public Review save(Review review) {
        return reviewDAO.save(review);
    }

    public void deleteById(Integer id) {
        reviewDAO.deleteById(id);
    }

    // Map Entity -> DTO để trả về Frontend
    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .rating(review.getRating())
                .comment(review.getComment())
                .productId(review.getProduct() != null ? review.getProduct().getProductId() : null)
                .userId(review.getUser() != null ? review.getUser().getUserId() : null)
                .reviewDate(review.getReviewDate())
                .userFullName(review.getUser() != null ? review.getUser().getFullName() : null)
                .userAvatar(review.getUser() != null ? review.getUser().getAvatar() : null)
                .productName(review.getProduct() != null ? review.getProduct().getName() : null)
                .productImage(review.getProduct() != null ? review.getProduct().getImageUrl() : null)
                .build();
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Integer productId) {
        return reviewDAO.findByProductId(productId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(Integer userId) {
        return reviewDAO.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        // Tìm User và Product từ ID mà Frontend gửi lên
        User user = userDAO.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Product product = productDAO.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Sản phẩm"));

        // Build Entity để lưu vào DB
        Review review = Review.builder()
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .user(user)
                .product(product)
                .build();

        Review savedReview = reviewDAO.save(review);

        return mapToDTO(savedReview);
    }
}