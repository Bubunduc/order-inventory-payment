package com.example.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.order.model.Order;

@Mapper
public interface OrderMapper {
	void insert(Order order);

	Order findById(@Param("orderId") Long orderId);

	void setAwaitingInventory(@Param("orderId") Long orderId);

	int setAwaitingPayment(@Param("orderId") Long orderId);

	int confirmOrder(@Param("orderId") Long orderId);

	int cancelOrder(@Param("orderId") Long orderId);
}
