package com.example.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.order.enums.OrderStatus;
import com.example.order.model.Order;

@Mapper
public interface OrderMapper {
	void insert(Order order);

	void updateStatus(@Param("id") Long id, @Param("status") OrderStatus status);
	
	Order findById(Long id);
}