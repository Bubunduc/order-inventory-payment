package com.example.order.service.impl;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.order.constants.RabbitConstants;
import com.example.order.dto.inventory.InventoryReleaseMessage;
import com.example.order.dto.order.OrderCreatedMessage;
import com.example.order.dto.payment.PaymentRefundMessage;
import com.example.order.service.MessageSender;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void sendCreateOrderMessage(OrderCreatedMessage message) {
		sendAsJson(RabbitConstants.SAGA_EXCHANGE, RabbitConstants.ORDER_CREATED, message);
	}

	@Override
	public void sendRefundMessage(Long orderId) {
		PaymentRefundMessage message = new PaymentRefundMessage(orderId);
		sendAsJson(RabbitConstants.SAGA_EXCHANGE, RabbitConstants.PAYMENT_REFUND, message);
	}

	@Override
	public void sendReleaseMessage(Long orderId) {
		InventoryReleaseMessage message = new InventoryReleaseMessage(orderId);
		sendAsJson(RabbitConstants.SAGA_EXCHANGE, RabbitConstants.INVENTORY_RELEASE, message);
	}

	private void sendAsJson(String exchange, String routingKey, Object payload) {
		byte[] body;

		try {
			body = objectMapper.writeValueAsBytes(payload);
		} catch (Exception e) {
			throw new RuntimeException("Не удалось сериализовать сообщение", e);
		}

		Message message = MessageBuilder.withBody(body).setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
		rabbitTemplate.send(exchange, routingKey, message);
	}

}
