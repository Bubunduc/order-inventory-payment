package com.example.inventory.service;

import java.math.BigDecimal;

public interface MessageSender {
	
	void sendReserveMessage(Long orderId,BigDecimal amount);
	void sendRejectMessage(Long orderId,String reason);
}
