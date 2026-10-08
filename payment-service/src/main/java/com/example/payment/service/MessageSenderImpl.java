package com.example.payment.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.payment.constants.RabbitConstants;
import com.example.payment.dto.PaymentCompletedMessage;
import com.example.payment.dto.PaymentFailedMessage;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void sendPaymentCompletedMessage(Long orderId) {
		PaymentCompletedMessage message = new PaymentCompletedMessage(orderId);
		sendAsJson(RabbitConstants.SAGA_EXCHANGE, RabbitConstants.PAYMENT_COMPLETED, message);

	}

	@Override
	public void sendPaymentFailedMessage(Long orderId, String reason) {
		PaymentFailedMessage message = new PaymentFailedMessage(orderId, reason);
		sendAsJson(RabbitConstants.SAGA_EXCHANGE, RabbitConstants.PAYMENT_FAILED, message);

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
