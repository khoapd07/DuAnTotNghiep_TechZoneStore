package com.poly.backend.repository;

import com.poly.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReportRepository extends JpaRepository<Order, Integer> {

    // 1. Lấy ra Top 5 Shipper giao được nhiều đơn hàng thành công nhất
    // SELECT COUNT: Đếm số lượng đơn. GROUP BY: Gom nhóm theo tên shipper. ORDER BY DESC: Sắp xếp giảm dần.
    @Query(value = "SELECT TOP 5 u.full_name as shipperName, COUNT(o.order_id) as totalOrders " +
            "FROM Orders o JOIN Users u ON o.shipper_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalOrders DESC", nativeQuery = true)
    List<Map<String, Object>> getTop5Shippers(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 2. Lấy ra Top 5 Khách hàng "VIP" chi nhiều tiền nhất
    // SUM(final_amount): Tính tổng tiền khách đã mua.
    @Query(value = "SELECT TOP 5 u.full_name as customerName, SUM(o.final_amount) as totalSpent " +
            "FROM Orders o JOIN Users u ON o.customer_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalSpent DESC", nativeQuery = true)
    List<Map<String, Object>> getTop5CustomersThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 3. Lấy ra Top 5 Sản phẩm bán chạy nhất (Tính theo Số lượng đã bán)
    // SUM(quantity): Tổng số lượng cái đã bán. SUM(quantity * price): Tổng doanh thu mang lại từ SP đó.
    @Query(value = "SELECT TOP 5 p.name as productName, SUM(od.quantity) as totalSold, SUM(od.quantity * od.price) as totalRevenue " +
            "FROM OrderDetails od JOIN Orders o ON od.order_id = o.order_id " +
            "JOIN Products p ON od.product_id = p.product_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY p.name ORDER BY totalSold DESC", nativeQuery = true)
    List<Map<String, Object>> getTop5ProductsThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 4. Lấy TỔNG DOANH THU của toàn hệ thống trong khoảng thời gian
    // Dùng ISNULL để nếu không có đơn nào thì trả về 0 chứ không bị lỗi NULL
    @Query(value = "SELECT ISNULL(SUM(final_amount), 0) FROM Orders " +
            "WHERE status_id = 3 AND order_date >= :startDate AND order_date <= :endDate", nativeQuery = true)
    Double getTotalRevenueThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 5. Lấy danh sách TẤT CẢ sản phẩm đã bán, SẮP XẾP THEO DOANH THU giảm dần (Tiền nhiều xếp trên)
    @Query(value = "SELECT p.name as productName, SUM(od.quantity) as totalSold, SUM(od.quantity * od.price) as totalRevenue " +
            "FROM OrderDetails od JOIN Orders o ON od.order_id = o.order_id " +
            "JOIN Products p ON od.product_id = p.product_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY p.name ORDER BY totalRevenue DESC", nativeQuery = true)
    List<Map<String, Object>> getSoldProductsThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 6. Lấy danh sách TẤT CẢ sản phẩm đã bán, SẮP XẾP THEO SỐ LƯỢNG giảm dần (Bán nhiều cái xếp trên)
    @Query(value = "SELECT p.name as productName, SUM(od.quantity) as totalSold, SUM(od.quantity * od.price) as totalRevenue " +
            "FROM OrderDetails od JOIN Orders o ON od.order_id = o.order_id " +
            "JOIN Products p ON od.product_id = p.product_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY p.name ORDER BY totalSold DESC", nativeQuery = true)
    List<Map<String, Object>> getAllProductsThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 7. Lấy danh sách TẤT CẢ khách hàng đã mua, xếp hạng theo độ chịu chi
    @Query(value = "SELECT u.full_name as customerName, SUM(o.final_amount) as totalSpent " +
            "FROM Orders o JOIN Users u ON o.customer_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalSpent DESC", nativeQuery = true)
    List<Map<String, Object>> getAllCustomersThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 8. Lấy danh sách TẤT CẢ Shipper của hệ thống, xếp hạng theo độ chăm chỉ giao đơn
    @Query(value = "SELECT u.full_name as shipperName, COUNT(o.order_id) as totalOrders " +
            "FROM Orders o JOIN Users u ON o.shipper_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "GROUP BY u.full_name ORDER BY totalOrders DESC", nativeQuery = true)
    List<Map<String, Object>> getAllShippers(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 9. Lấy chi tiết lịch sử từng đơn hàng giao thành công
    // Lấy mã đơn, tên khách, ngày mua và số tiền thu được. Sắp xếp theo ngày mới nhất lên đầu.
    @Query(value = "SELECT o.order_id as orderId, u.full_name as customerName, o.order_date as orderDate, o.final_amount as finalAmount " +
            "FROM Orders o JOIN Users u ON o.customer_id = u.user_id " +
            "WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate " +
            "ORDER BY o.order_date DESC", nativeQuery = true)
    List<Map<String, Object>> getSuccessfulOrdersThisMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 10. Thống kê số lượng đơn hàng theo từng tháng (Dùng cho biểu đồ cột/đường trên Frontend)
    @Query(value = "SELECT MONTH(order_date) as month, COUNT(order_id) as totalOrders " +
            "FROM Orders WHERE status_id = 3 AND order_date >= :startDate AND order_date <= :endDate " +
            "GROUP BY MONTH(order_date) ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> getOrdersPerMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 11. TRÙM CUỐI: Lấy số liệu Tổng Quan (Doanh thu, Đơn hàng, Lợi nhuận) hiển thị ở 3 thẻ Card trên cùng
    // - Dòng SELECT 1: Đếm tổng số đơn thành công.
    // - Dòng SELECT 2: Tính tổng doanh thu.
    // - Dòng SELECT 3: Tính Lợi Nhuận = (Giá bán - Giá nhập trung bình) * Số lượng.
    // - LEFT JOIN (Subquery): Gom toàn bộ phiếu nhập lại để tính ra "Giá nhập bình quân" của từng mặt hàng.
    @Query(value = "SELECT " +
            "(SELECT COUNT(order_id) FROM Orders WHERE status_id = 3 AND order_date >= :startDate AND order_date <= :endDate) as totalOrders, " +
            "(SELECT ISNULL(SUM(final_amount), 0) FROM Orders WHERE status_id = 3 AND order_date >= :startDate AND order_date <= :endDate) as totalRevenue, " +
            "(SELECT ISNULL(SUM((od.price - ISNULL(AvgCost.avg_import_price, 0)) * od.quantity), 0) " +
            " FROM OrderDetails od " +
            " JOIN Orders o ON od.order_id = o.order_id " +
            " LEFT JOIN (" +
            "     SELECT product_id, variant_id, " +
            "            SUM(quantity * import_price) / NULLIF(SUM(quantity), 0) AS avg_import_price " +
            "     FROM ImportReceiptDetails " +
            "     GROUP BY product_id, variant_id" +
            " ) AvgCost ON od.product_id = AvgCost.product_id AND ISNULL(od.variant_id, 0) = ISNULL(AvgCost.variant_id, 0) " +
            " WHERE o.status_id = 3 AND o.order_date >= :startDate AND o.order_date <= :endDate) as totalProfit",
            nativeQuery = true)
    Map<String, Object> getGeneralStats(@Param("startDate") String startDate, @Param("endDate") String endDate);
}