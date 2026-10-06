package com.example.payment.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.dto.InventoryReserveMessage;
import com.example.payment.emuns.PaymentStatus;
import com.example.payment.exception.PaymentFailedException;
import com.example.payment.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

	private final PaymentMapper paymentMapper;
	private final BigDecimal LIMIT = new BigDecimal("1000.0");
	
	@Override
	@Transactional
	public boolean pay(InventoryReserveMessage message) {
		Long orderId = message.orderId();
		BigDecimal amount = message.amount();
		int inserted = paymentMapper.insertIfAbsent(orderId, amount,PaymentStatus.PENDING);
		if (inserted == 0) {
			log.info("Заказ с order_id {} уже был обработан", orderId);
			return false;
		}
		if (amount.compareTo(LIMIT) < 0) {
			throw new PaymentFailedException("Оплата отклонена для заказа "+orderId);
		} 
		
		return true;
	}

	@Override
	@Transactional
	public void refund(Long orderId) {
		// TODO Auto-generated method stub
		
	}

}
