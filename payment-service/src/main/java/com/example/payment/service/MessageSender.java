package com.example.payment.service;

public interface MessageSender {
	void sendPaymentComplitedMessage(Long orderId);

	void sendPaymentFailedMessage(Long orderId,String reason);
}
