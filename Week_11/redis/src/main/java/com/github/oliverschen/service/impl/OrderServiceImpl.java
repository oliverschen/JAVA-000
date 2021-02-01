package com.github.oliverschen.service.impl;

import com.github.oliverschen.entity.Order;
import com.github.oliverschen.mapper.OrderMapper;
import com.github.oliverschen.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ck
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void insert(Order order) {
        orderMapper.insert(order);
    }

    @Override
    @Cacheable(key = "#id",value = "userCache")
    public Order get(Long id) {
        log.info("get in db ==> {}", id);
        return orderMapper.get(id);
    }

    @Override
    @CachePut(key = "#order.id",value = "userCache")
    public void update(Order order) {
        orderMapper.update(order);
    }

    @Override
    @CacheEvict(key = "#id",value = "userCache")
    public void del(Long id) {
        orderMapper.del(id);
    }

}
