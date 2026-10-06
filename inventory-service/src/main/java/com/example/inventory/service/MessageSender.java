package com.example.inventory.service;

public interface MessageSender {
	
	void sendReserveMessage(Long id);
	void sendRejectMessage(Long id,String reason);
}
