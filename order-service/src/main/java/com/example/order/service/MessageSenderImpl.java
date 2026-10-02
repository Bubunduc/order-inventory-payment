package com.example.order.service;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderCreatedMessage;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void sendMessage(Long orderId, CreateOrderRequest request) {
		OrderCreatedMessage message = new OrderCreatedMessage(
				orderId,
				request.items(),
				request.amount());
		try {
			byte[] body = objectMapper.writeValueAsBytes(message);

			rabbitTemplate.send(
					"saga.exchange",
					"order.created",
					MessageBuilder.
					withBody(body).
					setContentType(MessageProperties.CONTENT_TYPE_JSON).
					build());

		} catch (Exception e) {
			throw new RuntimeException("Не удалось сериализовать сообщение", e);
		}
	}
}
