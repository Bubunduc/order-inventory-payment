package com.example.order.service;

import com.example.order.dto.request.CreateOrderRequest;

public interface MessageSender {
	void sendCreateOrderMessage(Long orderId, CreateOrderRequest request);

	void sendRefundMessage(Long orderId);

	void sengReleaseMessage(Long orderId);
}
