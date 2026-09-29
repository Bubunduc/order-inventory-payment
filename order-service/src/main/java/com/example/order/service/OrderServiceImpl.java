package com.example.order.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.GetOrderRequest;

@Service
public class OrderServiceImpl implements OrderService {

	private final RabbitTemplate rabbitTemplate;

	public OrderServiceImpl(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	@Transactional
	public void createOrder(CreateOrderRequest request) {
		rabbitTemplate.convertAndSend("saga.exchange", "order.created", request);
	}

	@Override
	public GetOrderRequest getOrderById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
