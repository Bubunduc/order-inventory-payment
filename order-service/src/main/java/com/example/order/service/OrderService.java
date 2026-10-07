package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.GetOrderResponse;

public interface OrderService {

	void createOrder(CreateOrderRequest request);

	GetOrderResponse getOrderById(Long id);
	
	void setAwaitingPaymentStatus(Long id);
}
