package com.poly.backend.service.impl;

import com.poly.backend.dao.ReportDAO;
import com.poly.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDAO reportDAO;

    @Override
    public List<Map<String, Object>> getTop5Shippers(String startDate, String endDate) {
        return reportDAO.getTop5Shippers(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getTop5CustomersThisMonth(String startDate, String endDate) {
        return reportDAO.getTop5CustomersThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getTop5ProductsThisMonth(String startDate, String endDate) {
        return reportDAO.getTop5ProductsThisMonth(startDate, endDate);
    }

    @Override
    public Map<String, Object> getGeneralStats(String startDate, String endDate) {
        return reportDAO.getGeneralStats(startDate, endDate);
    }

    @Override
    public Map<String, Object> getTab2Details(String startDate, String endDate) {
        Map<String, Object> response = new HashMap<>();
        response.put("totalMonthlyIncome", reportDAO.getTotalRevenueThisMonth(startDate, endDate));
        response.put("soldProducts", reportDAO.getSoldProductsThisMonth(startDate, endDate));
        return response;
    }

    @Override
    public List<Map<String, Object>> getAllProductsThisMonth(String startDate, String endDate) {
        return reportDAO.getAllProductsThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getAllCustomersThisMonth(String startDate, String endDate) {
        return reportDAO.getAllCustomersThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getAllShippers(String startDate, String endDate) {
        return reportDAO.getAllShippers(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getSuccessfulOrdersThisMonth(String startDate, String endDate) {
        return reportDAO.getSuccessfulOrdersThisMonth(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getOrdersPerMonth(String startDate, String endDate) {
        return reportDAO.getOrdersPerMonth(startDate, endDate);
    }
}