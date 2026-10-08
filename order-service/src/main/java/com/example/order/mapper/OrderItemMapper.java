package com.example.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.order.model.OrderItem;

@Mapper
public interface OrderItemMapper {
    
    void insertAll(@Param("orderId") Long orderId, @Param("items") List<OrderItem> items);
}
