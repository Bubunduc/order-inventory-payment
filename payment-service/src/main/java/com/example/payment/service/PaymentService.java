package com.example.payment.service;

import com.example.payment.dto.InventoryReserveMessage;
import com.example.payment.dto.PaymentRefundMessage;
import com.example.payment.exception.PaymentFailedException;

public interface PaymentService {
	boolean pay(InventoryReserveMessage message) throws PaymentFailedException;
	void refund(PaymentRefundMessage message);
}
