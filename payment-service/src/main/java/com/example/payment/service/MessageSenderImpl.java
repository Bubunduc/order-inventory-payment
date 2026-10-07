package com.example.payment.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.payment.dto.PaymentComplitedMessage;
import com.example.payment.dto.PaymentFailedMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {
	
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	
	private final String EXCHANGE = "saga.exchange";
	private final String PAYMENT_COMPLITED = "payment.completed";
	private final String PAYMENT_FAILED = "payment.failed";
	
	@Override
	public void sendPaymentComplitedMessage(Long orderId) {
		PaymentComplitedMessage message = new PaymentComplitedMessage(orderId);
		sendAsJson(EXCHANGE,PAYMENT_COMPLITED,message);
		
	}

	@Override
	public void sendPaymentFailedMessage(Long orderId, String reason) {
		PaymentFailedMessage message = new PaymentFailedMessage(orderId, reason);
		sendAsJson(EXCHANGE,PAYMENT_FAILED,message);
		
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
