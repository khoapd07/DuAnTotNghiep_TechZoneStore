package com.poly.backend.repository;

import com.poly.backend.entity.SlideShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SlideShowRepository extends JpaRepository<SlideShow, Integer> {
    // Chỉ lấy những slide có active = true, sắp xếp theo thứ tự
    List<SlideShow> findByActiveTrueOrderByDisplayOrderAsc();
    // Lấy tất cả sắp xếp theo thứ tự cho trang Admin
    List<SlideShow> findAllByOrderByDisplayOrderAsc();
}