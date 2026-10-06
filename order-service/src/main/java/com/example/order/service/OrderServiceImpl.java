package com.example.order.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.GetOrderResponse;
import com.example.order.dto.OrderItemRequest;
import com.example.order.enums.OrderStatus;
import com.example.order.exception.DuplicateSkuException;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.Order;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final MessageSender messageSender;

	private final OrderMapper orderMapper;

	private final OrderItemMapper orderItemMapper;

	@Override
	@Transactional
	public void createOrder(CreateOrderRequest request) {
		List<String> skus = request.items().stream().map(OrderItemRequest::sku).toList();

		Set<String> uniqueSkus = new HashSet<>(skus);

		if (skus.size() != uniqueSkus.size()) {
			throw new DuplicateSkuException();
		}
		Order newOrder = new Order();
		newOrder.setAmount(request.amount());
		newOrder.setStatus(OrderStatus.CREATED);
		orderMapper.insert(newOrder);
		Long orderId = newOrder.getId();
		orderItemMapper.insertAll(orderId, OrderItemRequest.toEntityList(request.items()));
		messageSender.sendMessage(orderId, request);
		orderMapper.updateStatus(orderId, OrderStatus.AWAITING_INVENTORY);
	}

	@Override
	@Transactional(readOnly = true)
	public GetOrderResponse getOrderById(Long id) {
		Order order = orderMapper.findById(id);
		if (order == null) {
			throw new OrderNotFoundException(id);
		}
		return GetOrderResponse.fromEntity(order);
	}

}
