package com.example.inventory.service;

public interface MessageSender {
	
	void sendReserveMessage(Long orderId);
	void sendRejectMessage(Long orderId,String reason);
}
