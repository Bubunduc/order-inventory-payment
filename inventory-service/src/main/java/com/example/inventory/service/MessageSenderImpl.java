package com.example.inventory.service;

import java.math.BigDecimal;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.inventory.dto.InventoryRejectMessage;
import com.example.inventory.dto.InventoryReserveMessage;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	private final String EXCHANGE = "saga.exchange";
	private final String RESERVE_MESSAGE = "inventory.reserved";
	private final String REJECT_MESSAGE = "inventory.rejected";
	
	@Override
	public void sendReserveMessage(Long orderId, BigDecimal amount) {
		InventoryReserveMessage message = new InventoryReserveMessage(orderId, amount);
		sendAsJson(EXCHANGE, RESERVE_MESSAGE, message);
	}

	@Override
	public void sendRejectMessage(Long id, String reason) {
		InventoryRejectMessage message = new InventoryRejectMessage(id, reason);
		sendAsJson(EXCHANGE, REJECT_MESSAGE, message);
	}

	private void sendAsJson(String exchange, String routingKey, Object payload) {
		try {
			byte[] body = objectMapper.writeValueAsBytes(payload);
			Message message = MessageBuilder.
					withBody(body).
					setContentType(MessageProperties.CONTENT_TYPE_JSON)
					.build();
			rabbitTemplate.send(exchange, routingKey, message);

		} catch (Exception e) {
			throw new RuntimeException("Не удалось сериализовать и отправить сообщение в RabbitMQ", e);
		}
	}
}