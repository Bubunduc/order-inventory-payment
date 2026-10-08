package com.example.order.service;

import com.example.order.dto.order.GetOrderResponse;
import com.example.order.dto.request.CreateOrderRequest;

public interface OrderService {

	void createOrder(CreateOrderRequest request);

	GetOrderResponse getOrderById(Long id);
	
	void setAwaitingPaymentStatus(Long id);
	
	void startCompensation(Long id);
	
	void cancelRejectedOrder(Long id);
	
	void completeOrder(Long id);
	
}
