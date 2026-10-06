package com.example.payment.service;

import com.example.payment.dto.InventoryReserveMessage;

public interface PaymentService {
	boolean pay(InventoryReserveMessage message);
	void refund(Long orderId);
}
