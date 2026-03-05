package com.poly.backend.service.impl;

import com.poly.backend.dto.DashboardStatsDTO;
import com.poly.backend.dao.OrderDAO;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.poly.backend.dto.ChartDataDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;

    @Override
    public DashboardStatsDTO getStats() {
        // 1. Xác định mốc thời gian
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        LocalDateTime fourteenDaysAgo = now.minusDays(14);

        // --- TÍNH TOÁN DOANH THU (7 ngày qua vs 7 ngày trước đó) ---
        BigDecimal revThisPeriod = orderDAO.sumTotalRevenueBetween(sevenDaysAgo, now);
        BigDecimal revLastPeriod = orderDAO.sumTotalRevenueBetween(fourteenDaysAgo, sevenDaysAgo);

        double revenueGrowth = calculateGrowth(revThisPeriod.doubleValue(), revLastPeriod.doubleValue());

        // --- TÍNH TOÁN ĐƠN HÀNG (7 ngày qua vs 7 ngày trước đó) ---
        Long ordersThisPeriod = orderDAO.countOrdersBetween(sevenDaysAgo, now);
        Long ordersLastPeriod = orderDAO.countOrdersBetween(fourteenDaysAgo, sevenDaysAgo);

        double orderGrowth = calculateGrowth(ordersThisPeriod.doubleValue(), ordersLastPeriod.doubleValue());

        // --- TÍNH SẢN PHẨM HẾT HÀNG (Lấy ở hiện tại) ---
        Long outOfStock = productDAO.countByStockQuantityLessThan(16);

        return DashboardStatsDTO.builder()
                .totalRevenue(revThisPeriod.doubleValue()) // Trả về doanh thu 7 ngày qua
                .revenueGrowth(revenueGrowth)
                .newOrdersCount(ordersThisPeriod)          // Trả về số đơn 7 ngày qua
                .orderGrowth(orderGrowth)
                .outOfStockCount(outOfStock)               // Hết hàng hiện tại
                .build();
    }

    // Hàm Helper tính toán % tăng trưởng
    private double calculateGrowth(double current, double previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        double growth = ((current - previous) / previous) * 100;

        // Làm tròn 1 chữ số thập phân (Ví dụ: 12.556 -> 12.6)
        BigDecimal bd = new BigDecimal(Double.toString(growth));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public List<ChartDataDTO> getChartData7Days() {
        List<ChartDataDTO> chartDataList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Chạy vòng lặp từ 6 về 0 để lấy 7 ngày (từ quá khứ đến hiện tại)
        for (int i = 6; i >= 0; i--) {
            LocalDateTime targetDate = now.minusDays(i);

            // Lấy 00:00:00 và 23:59:59 của ngày đó
            LocalDateTime startOfDay = targetDate.with(LocalTime.MIN);
            LocalDateTime endOfDay = targetDate.with(LocalTime.MAX);

            // 1. Lấy doanh thu của ngày đó
            BigDecimal dailyRevenue = orderDAO.sumTotalRevenueBetween(startOfDay, endOfDay);
            double revenueValue = (dailyRevenue != null) ? dailyRevenue.doubleValue() : 0.0;

            // 2. Lấy số đơn HOÀN THÀNH của ngày đó
            Long dailyOrderCount = orderDAO.countOrdersBetween(startOfDay, endOfDay);

            // 3. Tạo nhãn (Label) cho trục X.
            // Nếu muốn format kiểu "01/03", "02/03" thì dùng dòng này:
            String label = targetDate.format(DateTimeFormatter.ofPattern("dd/MM"));

            // (Tuỳ chọn) Nếu bạn thích hiển thị "T2, T3, CN" y hệt ảnh thiết kế thì dùng cái này thay thế:
            // String label = getVietnameseDayOfWeek(targetDate.getDayOfWeek().getValue());

            chartDataList.add(new ChartDataDTO(label, revenueValue, dailyOrderCount));
        }

        return chartDataList;
    }

    // Hàm Helper (Nếu bạn dùng tùy chọn "T2, T3...")
    private String getVietnameseDayOfWeek(int dayValue) {
        switch (dayValue) {
            case 1: return "T2";
            case 2: return "T3";
            case 3: return "T4";
            case 4: return "T5";
            case 5: return "T6";
            case 6: return "T7";
            case 7: return "CN";
            default: return "";
        }
    }
}