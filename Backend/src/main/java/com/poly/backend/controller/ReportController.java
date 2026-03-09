package com.poly.backend.controller;

import com.poly.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/top-shippers")
    public ResponseEntity<List<Map<String, Object>>> getTop5Shippers(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getTop5Shippers(startDate, endDate));
    }

    @GetMapping("/top-customers-month")
    public ResponseEntity<List<Map<String, Object>>> getTop5CustomersThisMonth(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getTop5CustomersThisMonth(startDate, endDate));
    }

    @GetMapping("/top-products-month")
    public ResponseEntity<List<Map<String, Object>>> getTop5ProductsThisMonth(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getTop5ProductsThisMonth(startDate, endDate));
    }

    @GetMapping("/general-stats")
    public ResponseEntity<Map<String, Object>> getGeneralStats(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getGeneralStats(startDate, endDate));
    }

    @GetMapping("/tab2-details")
    public ResponseEntity<Map<String, Object>> getTab2Details(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getTab2Details(startDate, endDate));
    }

    @GetMapping("/all-products-month")
    public ResponseEntity<List<Map<String, Object>>> getAllProductsThisMonth(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getAllProductsThisMonth(startDate, endDate));
    }

    @GetMapping("/all-customers-month")
    public ResponseEntity<List<Map<String, Object>>> getAllCustomersThisMonth(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getAllCustomersThisMonth(startDate, endDate));
    }

    @GetMapping("/all-shippers")
    public ResponseEntity<List<Map<String, Object>>> getAllShippers(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getAllShippers(startDate, endDate));
    }

    @GetMapping("/successful-orders-month")
    public ResponseEntity<List<Map<String, Object>>> getSuccessfulOrdersThisMonth(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getSuccessfulOrdersThisMonth(startDate, endDate));
    }
}