package com.github.oliverschen.service;


import com.github.oliverschen.entity.Order;

/**
 * @author ck
 */
public interface OrderService {

    void insert(Order order);

    Order get(Long id);
}
