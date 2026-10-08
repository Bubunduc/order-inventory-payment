package com.example.payment.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.dto.InventoryReserveMessage;
import com.example.payment.dto.PaymentRefundMessage;
import com.example.payment.enums.PaymentStatus;
import com.example.payment.exception.PaymentFailedException;
import com.example.payment.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentMapper paymentMapper;
	
	@Value("${payment.limit}")
	private BigDecimal paymentLimit;
	
	@Override
	@Transactional
	public boolean pay(InventoryReserveMessage message) throws PaymentFailedException {
		Long orderId = message.orderId();
		BigDecimal amount = message.amount();
		int inserted = paymentMapper.insertIfAbsent(
				orderId,
				amount,
				PaymentStatus.PENDING
			);
		if (inserted == 0) {
			log.info("Заказ с order_id {} уже был обработан", orderId);
			return false;
		}
		if (amount.compareTo(paymentLimit) > 0) {
			int failed = paymentMapper.failPayment(orderId);
			if (failed != 0) {
				throw new PaymentFailedException ("Оплата отклонена для заказа " + orderId);
			}
			else {
				log.info("Заказ с order_id {} имеет не соответствующий действию статус", orderId);
				return false;
			}
			
		}
		int paid = paymentMapper.completePayment(orderId);
		if (paid == 0) {
			log.info("Заказ с order_id {} имеет не соответствующий действию статус", orderId);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public void refund(PaymentRefundMessage message) {
		int refunded = paymentMapper.refund(message.orderId());
		if (refunded == 0) {
			log.info("Refund для заказа {} не выполнен, так как статус не FAILED",message.orderId());
		} 
		
	}

}
