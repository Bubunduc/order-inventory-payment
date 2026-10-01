package com.example.order.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderCreatedMessage;
import com.example.order.enums.OrderStatus;
import com.example.order.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

	private final RabbitTemplate rabbitTemplate;

	private final OrderMapper orderMapper;

	@Override
	@Transactional
	public void sendMessage(Long orderId, CreateOrderRequest request) {
		OrderCreatedMessage message = new OrderCreatedMessage(orderId, request.items(), request.amount());
		rabbitTemplate.convertAndSend("saga.exchange", "order.created", message);
		orderMapper.updateStatus(orderId, OrderStatus.AWAITING_INVENTORY);
	}
}
