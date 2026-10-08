package com.example.order.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.dto.order.OrderCreatedMessage;
import com.example.order.dto.order.OrderCreatedMessageItem;
import com.example.order.dto.request.CreateOrderRequest;
import com.example.order.dto.request.OrderItemRequest;
import com.example.order.enums.OrderStatus;
import com.example.order.exception.DuplicateSkuException;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.Order;
import com.example.order.service.OrderTransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderTransactionServiceImpl implements OrderTransactionService {

	private final OrderMapper orderMapper;
	private final OrderItemMapper orderItemMapper;

	@Transactional
	@Override
	public OrderCreatedMessage createOrder(CreateOrderRequest request) {

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

		orderMapper.setAwaitingInventory(orderId);

		List<OrderCreatedMessageItem> messageItems = request.items().stream()
				.map(item -> new OrderCreatedMessageItem(item.sku(), item.qty())).toList();

		return new OrderCreatedMessage(orderId, messageItems, request.amount());
	}
}
