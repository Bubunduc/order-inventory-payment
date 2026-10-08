package com.example.inventory.service;

import java.math.BigDecimal;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.inventory.constants.RabbitConstants;
import com.example.inventory.dto.InventoryRejectMessage;
import com.example.inventory.dto.InventoryReserveMessage;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void sendReserveMessage(Long orderId, BigDecimal amount) {
		InventoryReserveMessage message = new InventoryReserveMessage(orderId, amount);
		sendAsJson(RabbitConstants.SAGA_EXCHANGE, RabbitConstants.INVENTORY_RESERVED, message);
	}

	@Override
	public void sendRejectMessage(Long id, String reason) {
		InventoryRejectMessage message = new InventoryRejectMessage(id, reason);
		sendAsJson(RabbitConstants.SAGA_EXCHANGE, RabbitConstants.INVENTORY_REJECTED, message);
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
