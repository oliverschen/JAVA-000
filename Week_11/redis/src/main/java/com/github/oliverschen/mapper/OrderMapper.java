package com.github.oliverschen.mapper;

import com.github.oliverschen.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ck
 */
@Mapper
public interface OrderMapper {

    void insert(Order order);

    Order get(Long id);
}
