package com.poly.backend.dao;

import com.poly.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReportDAO extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT TOP 5 u.full_name as shipperName, COUNT(o.order_id) as totalOrders " +
            "FROM Orders o JOIN Users u ON o.shipper_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalOrders DESC", nativeQuery = true)
    List<Map<String, Object>> getTop5Shippers(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT TOP 5 u.full_name as customerName, SUM(o.final_amount) as totalSpent " +
            "FROM Orders o JOIN Users u ON o.customer_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalSpent DESC", nativeQuery = true)
    List<Map<String, Object>> getTop5CustomersThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT TOP 5 p.name as productName, SUM(od.quantity) as totalSold, SUM(od.quantity * od.price) as totalRevenue " +
            "FROM OrderDetails od JOIN Orders o ON od.order_id = o.order_id " +
            "JOIN Products p ON od.product_id = p.product_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY p.name ORDER BY totalSold DESC", nativeQuery = true)
    List<Map<String, Object>> getTop5ProductsThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT COUNT(order_id) as totalOrders, ISNULL(SUM(final_amount), 0) as totalRevenue " +
            "FROM Orders WHERE status_id = 3 AND order_date >= :startDate AND order_date <= :endDate", nativeQuery = true)
    Map<String, Object> getGeneralStats(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT ISNULL(SUM(final_amount), 0) FROM Orders " +
            "WHERE status_id = 3 AND order_date >= :startDate AND order_date <= :endDate", nativeQuery = true)
    Double getTotalRevenueThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT p.name as productName, SUM(od.quantity) as totalSold, SUM(od.quantity * od.price) as totalRevenue " +
            "FROM OrderDetails od JOIN Orders o ON od.order_id = o.order_id " +
            "JOIN Products p ON od.product_id = p.product_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY p.name ORDER BY totalRevenue DESC", nativeQuery = true)
    List<Map<String, Object>> getSoldProductsThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT p.name as productName, SUM(od.quantity) as totalSold, SUM(od.quantity * od.price) as totalRevenue " +
            "FROM OrderDetails od JOIN Orders o ON od.order_id = o.order_id " +
            "JOIN Products p ON od.product_id = p.product_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY p.name ORDER BY totalSold DESC", nativeQuery = true)
    List<Map<String, Object>> getAllProductsThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT u.full_name as customerName, SUM(o.final_amount) as totalSpent " +
            "FROM Orders o JOIN Users u ON o.customer_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalSpent DESC", nativeQuery = true)
    List<Map<String, Object>> getAllCustomersThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT u.full_name as shipperName, COUNT(o.order_id) as totalOrders " +
            "FROM Orders o JOIN Users u ON o.shipper_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalOrders DESC", nativeQuery = true)
    List<Map<String, Object>> getAllShippers(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT o.order_id as orderId, u.full_name as customerName, o.order_date as orderDate, o.final_amount as finalAmount " +
            "FROM Orders o JOIN Users u ON o.customer_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "ORDER BY o.order_date DESC", nativeQuery = true)
    List<Map<String, Object>> getSuccessfulOrdersThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // Bổ sung hàm này để tránh bị báo lỗi đỏ ở ServiceImpl
    @Query(value = "SELECT MONTH(order_date) as month, COUNT(order_id) as totalOrders " +
            "FROM Orders WHERE status_id = 3 AND order_date >= :startDate AND order_date <= :endDate " +
            "GROUP BY MONTH(order_date) ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> getOrdersPerMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
}