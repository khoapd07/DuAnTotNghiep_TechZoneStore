package com.poly.backend.dao;

import com.poly.backend.dto.OrderResponseDTO;
import com.poly.backend.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    // Tìm lịch sử đơn hàng của 1 User, xếp mới nhất lên đầu
    List<Order> findByCustomer_UserIdOrderByOrderDateDesc(Integer userId);

    Optional<Order> findByOrderCode(String orderCode);

    // --- BỔ SUNG CHO DASHBOARD ---

    @Query("SELECT SUM(o.finalAmount) FROM Order o WHERE o.status.statusName = 'HOÀN THÀNH'")
    Double sumTotalRevenue();

    // 2. Sửa o.orderStatus.name -> o.status.statusName
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status.statusName = :statusName")
    Long countOrdersByStatusName(@Param("statusName") String statusName);

    // 3. Giữ nguyên hàm lấy danh sách Entity
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(Pageable pageable);
}