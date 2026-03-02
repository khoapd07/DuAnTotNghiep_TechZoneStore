package com.poly.backend.dao;

import com.poly.backend.entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDAO extends JpaRepository<Cart, Integer> {

    // --- CẬP NHẬT MỚI: Dành cho logic Service chúng ta vừa viết ---
    // Vì mỗi User giờ chỉ có 1 Giỏ hàng (1-1), ta trả về Optional<Cart> thay vì List<Cart>
    Optional<Cart> findByUser_UserId(Integer userId);

    // --- GIỮ LẠI LOGIC CŨ CỦA BẠN (Đã sửa Entity Customer -> User) ---
    // Phòng trường hợp ở các file khác bạn vẫn đang gọi hàm delete này
    @Transactional
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId")
    void deleteByUserId(@Param("userId") Integer userId);


}