package com.poly.backend.service.impl;

import com.poly.backend.repository.ReportRepository;
import com.poly.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<Map<String, Object>> getTop5Shippers(String startDate, String endDate) {
        return reportRepository.getTop5Shippers(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getTop5CustomersThisMonth(String startDate, String endDate) {
        return reportRepository.getTop5CustomersThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getTop5ProductsThisMonth(String startDate, String endDate) {
        return reportRepository.getTop5ProductsThisMonth(startDate, endDate);
    }

    @Override
    public Map<String, Object> getGeneralStats(String startDate, String endDate) {
        return reportRepository.getGeneralStats(startDate, endDate);
    }

    @Override
    public Map<String, Object> getTab2Details(String startDate, String endDate) {
        Map<String, Object> response = new HashMap<>();
        response.put("totalMonthlyIncome", reportRepository.getTotalRevenueThisMonth(startDate, endDate));
        response.put("soldProducts", reportRepository.getSoldProductsThisMonth(startDate, endDate));
        return response;
    }

    @Override
    public List<Map<String, Object>> getAllProductsThisMonth(String startDate, String endDate) {
        return reportRepository.getAllProductsThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getAllCustomersThisMonth(String startDate, String endDate) {
        return reportRepository.getAllCustomersThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getAllShippers(String startDate, String endDate) {
        return reportRepository.getAllShippers(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getSuccessfulOrdersThisMonth(String startDate, String endDate) {
        return reportRepository.getSuccessfulOrdersThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getOrdersPerMonth(String startDate, String endDate) {
        return reportRepository.getOrdersPerMonth(startDate, endDate);
    }
}