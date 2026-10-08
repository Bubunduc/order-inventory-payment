package com.example.order.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.InventoryReleaseMessage;
import com.example.order.dto.OrderCreatedMessage;
import com.example.order.dto.PaymentRefundMessage;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	
	private static final String EXCHANGE = "saga.exchange";
	private static final String ORDER_CREATED = "order.created";
	private static final String PAYMENT_REFUND = "payment.refund";
	private static final String INVENTORY_RELEASE = "inventory.release";

	@Override
	public void sendCreateOrderMessage(Long orderId, CreateOrderRequest request) {
		OrderCreatedMessage message = new OrderCreatedMessage(orderId, request.items(), request.amount());
		sendAsJson(EXCHANGE, ORDER_CREATED, message);
	}
	
	@Override
	public void sendRefundMessage(Long orderId) {
		PaymentRefundMessage message = new PaymentRefundMessage(orderId);
		sendAsJson(EXCHANGE, PAYMENT_REFUND, message);
	}
	@Override
	public void sengReleaseMessage(Long orderId) {
		InventoryReleaseMessage message = new InventoryReleaseMessage(orderId);
		sendAsJson(EXCHANGE, INVENTORY_RELEASE, message);
	}
	
	private void sendAsJson(String exchange, String routingKey, Object payload) {
		try {
			byte[] body = objectMapper.writeValueAsBytes(payload);
			Message message = MessageBuilder.
					withBody(body).
					setContentType(MessageProperties.CONTENT_TYPE_JSON).
					build();
			rabbitTemplate.send(exchange, routingKey, message);

		} catch (Exception e) {
			throw new RuntimeException("Не удалось сериализовать и отправить сообщение в RabbitMQ", e);
		}
	}

}
