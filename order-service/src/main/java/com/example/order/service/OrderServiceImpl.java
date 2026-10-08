package com.example.order.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.dto.order.GetOrderResponse;
import com.example.order.dto.order.OrderCreatedMessage;
import com.example.order.dto.order.OrderCreatedMessageItem;
import com.example.order.dto.request.CreateOrderRequest;
import com.example.order.dto.request.OrderItemRequest;
import com.example.order.enums.OrderStatus;
import com.example.order.exception.DuplicateSkuException;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.Order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final MessageSender messageSender;

	private final OrderMapper orderMapper;

	private final OrderItemMapper orderItemMapper;

	private final OrderTransactionService orderTransactionService;

	@Override
	public void createOrder(CreateOrderRequest request) {

		OrderCreatedMessage message = orderTransactionService.createOrder(request);

		messageSender.sendCreateOrderMessage(message);
	}

	@Transactional
	private OrderCreatedMessage insertOrder(CreateOrderRequest request) {
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
		List<OrderCreatedMessageItem> messageItems = request.items().stream()
				.map(item -> new OrderCreatedMessageItem(item.sku(), item.qty())).toList();

		OrderCreatedMessage message = new OrderCreatedMessage(orderId, messageItems, request.amount());
		orderMapper.setAwaitingInventory(message.orderId());
		return message;

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

	@Override
	public void setAwaitingPaymentStatus(Long id) {
		int awaiting = orderMapper.setAwaitingPayment(id);
		checkStatus(awaiting, id);

	}

	@Override
	public void startCompensation(Long id) {
		int orderCaneled = orderMapper.cancelOrder(id);
		if (checkStatus(orderCaneled, id)) {
			messageSender.sengReleaseMessage(id);
			messageSender.sendRefundMessage(id);
		}
	}

	@Override
	public void cancelRejectedOrder(Long id) {
		int orderCaneled = orderMapper.cancelOrder(id);
		checkStatus(orderCaneled, id);
	}

	@Override
	public void completeOrder(Long id) {
		int confirmed = orderMapper.confirmOrder(id);
		checkStatus(confirmed, id);
	}

	private boolean checkStatus(int queryResult, Long id) {
		if (queryResult == 0) {
			log.info("Заказ с id {} имеет не соответствующий действию статус", id);
			return false;
		}
		return true;
	}

}
