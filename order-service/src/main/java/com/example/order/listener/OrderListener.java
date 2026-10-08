package com.example.order.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.order.constants.RabbitConstants;
import com.example.order.dto.inventory.InventoryRejectMessage;
import com.example.order.dto.payment.PaymentCompletedMessage;
import com.example.order.dto.payment.PaymentFailedMessage;
import com.example.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {
	private final OrderService orderService;
	private final ObjectMapper objectMapper;
	
	@RabbitListener(queues = RabbitConstants.ORDER_QUEUE)
	public void listen(Message message) {
		String routingKey = message.getMessageProperties().getReceivedRoutingKey();

		switch (routingKey) {

		case RabbitConstants.INVENTORY_REJECTED:
			handleInventoryRejected(message);
			break;

		case RabbitConstants.PAYMENT_COMPLETED:
			handlePaymentCompleted(message);
			break;
		case RabbitConstants.PAYMENT_FAILED:
			handlePaymentFailed(message);
			break;

		default:
			log.warn("Получено сообщение с неизвестным routing key: {}", routingKey);
		}
	}

	private void handlePaymentFailed(Message message) {
		PaymentFailedMessage paymentFailedMessage = objectMapper.readValue(message.getBody(), PaymentFailedMessage.class);
		orderService.startCompensation(paymentFailedMessage.orderId());
	}

	private void handlePaymentCompleted(Message message) {
		PaymentCompletedMessage paymentCompletedMessage = objectMapper.readValue(message.getBody(), PaymentCompletedMessage.class);
		orderService.completeOrder(paymentCompletedMessage.orderId());
	}

	private void handleInventoryRejected(Message message) {
		InventoryRejectMessage inventoryRejectMessage = objectMapper.readValue(message.getBody(), InventoryRejectMessage.class);
		orderService.cancelRejectedOrder(inventoryRejectMessage.orderId());
	}
}
