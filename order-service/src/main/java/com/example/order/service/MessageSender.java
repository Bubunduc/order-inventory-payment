package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;

public interface MessageSender {
	void sendMessage(Long orderId,CreateOrderRequest request);
}
