package com.example.payment.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;

import com.example.payment.dto.InventoryReserveMessage;
import com.example.payment.dto.PaymentRefundMessage;
import com.example.payment.exception.PaymentFailedException;
import com.example.payment.service.MessageSender;
import com.example.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener {
	
	private final ObjectMapper objectMapper;
	private final PaymentService paymentService;
	private final MessageSender messageSender;
	
	@RabbitListener(queues = "payment.queue")
	public void listen(Message message) {
		String routingKey = message.getMessageProperties().getReceivedRoutingKey();

		switch (routingKey) {

		case "inventory.reserved":
			handleInventoryReserved(message);
			break;

		case "payment.refund":
			handlePaymentRefund(message);
			break;

		default:
			log.warn("Получено сообщение с неизвестным routing key: {}", routingKey);
		}
	}

	private void handleInventoryReserved(Message message) {
		InventoryReserveMessage inventoryReserveMessage = objectMapper.readValue(message.getBody(), InventoryReserveMessage.class);
		try {
			boolean paid = paymentService.pay(inventoryReserveMessage);
			
			if (!paid) {
			    return;
			}
			
			messageSender.sendPaymentCompletedMessage(inventoryReserveMessage.orderId());
		}catch (PaymentFailedException e) {
			messageSender.sendPaymentFailedMessage(inventoryReserveMessage.orderId(),e.getMessage());
		}catch (DataAccessException e) {
			log.error("Ошибка бд при обработке заказа {}", inventoryReserveMessage.orderId(), e);
			throw e;
		} catch (TransactionException e) {
			log.error("Не удалось выполнить транзакцию для заказа {}", inventoryReserveMessage.orderId(), e);
			throw e;
		}
	}

	private void handlePaymentRefund(Message message) {
		PaymentRefundMessage refundMessage = objectMapper.readValue(message.getBody(), PaymentRefundMessage.class);
		try {
			paymentService.refund(refundMessage);
		}catch (DataAccessException e) {
			log.error("Ошибка бд при обработке заказа {}", refundMessage.orderId(), e);
			throw e;
		} catch (TransactionException e) {
			log.error("Не удалось выполнить транзакцию для заказа {}", refundMessage.orderId(), e);
			throw e;
		}
	}
}
