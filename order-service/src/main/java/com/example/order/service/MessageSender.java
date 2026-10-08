package com.example.order.service;

import com.example.order.dto.order.OrderCreatedMessage;

public interface MessageSender {
	void sendCreateOrderMessage(OrderCreatedMessage message);

	void sendRefundMessage(Long orderId);

	void sengReleaseMessage(Long orderId);
}
