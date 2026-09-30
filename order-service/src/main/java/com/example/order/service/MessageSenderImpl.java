package com.example.order.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderCreatedMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender{
	
	private final RabbitTemplate rabbitTemplate;

	@Override
	public void sendMessage(Long orderId,CreateOrderRequest request) {
		OrderCreatedMessage message = new OrderCreatedMessage(orderId, request.items(), request.amount());
		rabbitTemplate.convertAndSend("saga.exchange", "order.created", message);
		
	}

}
