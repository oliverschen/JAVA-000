package com.github.oliverschen.service.impl;

import com.github.oliverschen.entity.Order;
import com.github.oliverschen.mapper.OrderMapper;
import com.github.oliverschen.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ck
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void insert(Order order) {
        orderMapper.insert(order);
    }

    @Override
    public Order get(Long id) {
        return orderMapper.get(id);
    }

}
