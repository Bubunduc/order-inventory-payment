package com.example.payment.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.payment.dto.InventoryReserveMessage;
import com.example.payment.exception.PaymentFailedException;
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
		InventoryReserveMessage orderCreatedMessage = objectMapper.readValue(message.getBody(), InventoryReserveMessage.class);
		try {
			paymentService.pay(orderCreatedMessage);
		}catch (PaymentFailedException e) {
			// TODO: handle exception
		}
	}

	private void handlePaymentRefund(Message message) {
	
	}
}
