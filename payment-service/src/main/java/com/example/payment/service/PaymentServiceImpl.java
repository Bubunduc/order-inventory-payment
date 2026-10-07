package com.example.payment.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.dto.InventoryReserveMessage;
import com.example.payment.dto.PaymentRefundMessage;
import com.example.payment.enuns.PaymentStatus;
import com.example.payment.exception.PaymentFailedException;
import com.example.payment.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

	private final PaymentMapper paymentMapper;
	private static final BigDecimal LIMIT = new BigDecimal("1000.0"); //Если выше, то зазказ не принят
	
	@Override
	@Transactional
	public boolean pay(InventoryReserveMessage message) throws PaymentFailedException {
		Long orderId = message.orderId();
		BigDecimal amount = message.amount();
		int inserted = paymentMapper.insertIfAbsent(orderId, amount,PaymentStatus.PENDING);
		if (inserted == 0) {
			log.info("Заказ с order_id {} уже был обработан", orderId);
			return false;
		}
		if (amount.compareTo(LIMIT) > 0) {
			paymentMapper.updateStatus(orderId, PaymentStatus.FAILED);
			throw new PaymentFailedException("Оплата отклонена для заказа " + orderId);
		} 
		paymentMapper.updateStatus(orderId, PaymentStatus.COMPLETED);
		return true;
	}

	@Override
	@Transactional
	public void refund(PaymentRefundMessage message) {
		paymentMapper.updateStatus(message.orderId(), PaymentStatus.REFUNDED);
		
	}

}
