package com.poly.backend.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> getTop5Shippers(String startDate, String endDate);
    List<Map<String, Object>> getTop5CustomersThisMonth(String startDate, String endDate);
    List<Map<String, Object>> getTop5ProductsThisMonth(String startDate, String endDate);
    List<Map<String, Object>> getOrdersPerMonth(String startDate, String endDate);
    Map<String, Object> getGeneralStats(String startDate, String endDate);

    Map<String, Object> getTab2Details(String startDate, String endDate);

    List<Map<String, Object>> getAllProductsThisMonth(String startDate, String endDate);
    List<Map<String, Object>> getAllCustomersThisMonth(String startDate, String endDate);
    List<Map<String, Object>> getAllShippers(String startDate, String endDate);
    List<Map<String, Object>> getSuccessfulOrdersThisMonth(String startDate, String endDate);
}