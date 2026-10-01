package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.GetOrderRequest;

public interface OrderService {

	void createOrder(CreateOrderRequest request);

	GetOrderRequest getOrderById(Long id);
}
