package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.OrderDetailDAO;
import com.poly.backend.entity.OrderDetail;
import com.poly.backend.service.OrderDetailService;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailDAO orderDetailDAO;

    public List<OrderDetail> findAll() {
        return orderDetailDAO.findAll();
    }

    public OrderDetail findById(Integer id) {
        return orderDetailDAO.findById(id).orElse(null);
    }

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailDAO.save(orderDetail);
    }

    public void deleteById(Integer id) {
        orderDetailDAO.deleteById(id);
    }
}