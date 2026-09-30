package com.example.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.GetOrderRequest;
import com.example.order.enums.OrderStatus;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.Order;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final MessageSender messageSender;

	private final OrderMapper orderMapper;

	@Override
	@Transactional
	public void createOrder(CreateOrderRequest request) {

		Order newOrder = new Order();
		newOrder.setAmount(request.amount());
		newOrder.setStatus(OrderStatus.CREATED);
		orderMapper.insert(newOrder);
		Long orderId = newOrder.getId();
		messageSender.sendMessage(orderId, request);

	}

	@Override
	public GetOrderRequest getOrderById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
