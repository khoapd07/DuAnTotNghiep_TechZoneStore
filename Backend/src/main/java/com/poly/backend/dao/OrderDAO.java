package com.poly.backend.dao;

import com.poly.backend.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {

    // Tìm lịch sử đơn hàng của 1 User, xếp mới nhất lên đầu
    List<Order> findByCustomer_UserIdOrderByOrderDateDesc(Integer userId);

    Optional<Order> findByOrderCode(String orderCode);

    // --- BỔ SUNG CHO DASHBOARD ---

    // Tính tổng doanh thu tất cả thời gian (Chỉ tính đơn HOÀN THÀNH)
    @Query("SELECT SUM(o.finalAmount) FROM Order o WHERE o.status.statusName = 'HOÀN THÀNH'")
    Double sumTotalRevenue();

    // Đếm số lượng đơn hàng theo trạng thái
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status.statusName = :statusName")
    Long countOrdersByStatusName(@Param("statusName") String statusName);

    // Lấy danh sách đơn hàng gần đây (không giới hạn thời gian)
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(Pageable pageable);


    // --- THỐNG KÊ CHO 7 NGÀY GẦN NHẤT ---

    // 1. Tính tổng doanh thu (finalAmount) trong một khoảng thời gian
    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate <= :endDate")
    BigDecimal sumTotalRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 2. Đếm số lượng đơn hàng trong một khoảng thời gian
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate <= :endDate")
    Long countOrdersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 3. Lấy danh sách đơn hàng gần đây NHẤT trong một khoảng thời gian (Dùng cho bảng Đơn hàng gần đây trên Dashboard)
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate <= :endDate ORDER BY o.orderDate DESC")
    List<Order> findRecentOrdersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    // Đếm số lượng đơn hàng theo khoảng thời gian VÀ theo trạng thái
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate <= :endDate AND o.status.statusName = :statusName")
    Long countOrdersBetweenAndStatus(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("statusName") String statusName);
}