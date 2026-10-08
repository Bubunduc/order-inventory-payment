package com.example.order.service;

import com.example.order.dto.order.OrderCreatedMessage;
import com.example.order.dto.request.CreateOrderRequest;

public interface OrderTransactionService {
	OrderCreatedMessage createOrder(CreateOrderRequest request);
}
