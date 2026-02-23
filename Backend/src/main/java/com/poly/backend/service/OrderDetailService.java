package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.OrderDetail;

public interface OrderDetailService {
    List<OrderDetail> findAll();
    OrderDetail findById(Integer id);
    OrderDetail save(OrderDetail orderDetail);
    void deleteById(Integer id);
}