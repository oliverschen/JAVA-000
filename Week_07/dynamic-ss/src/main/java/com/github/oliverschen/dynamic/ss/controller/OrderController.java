package com.github.oliverschen.dynamic.ss.controller;

import com.github.oliverschen.dynamic.ss.entity.Order;
import com.github.oliverschen.dynamic.ss.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ck
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/insert")
    public String insert(@RequestBody Order order) {
        orderService.insert(order);
        return "OK";
    }


}
