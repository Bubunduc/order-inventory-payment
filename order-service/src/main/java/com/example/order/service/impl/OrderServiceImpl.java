package com.example.order.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.dto.order.GetOrderResponse;
import com.example.order.dto.order.OrderCreatedMessage;
import com.example.order.dto.request.CreateOrderRequest;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.Order;
import com.example.order.service.MessageSender;
import com.example.order.service.OrderService;
import com.example.order.service.OrderTransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final MessageSender messageSender;

	private final OrderMapper orderMapper;

	private final OrderTransactionService orderTransactionService;

	@Override
	public void createOrder(CreateOrderRequest request) {

		OrderCreatedMessage message = orderTransactionService.createOrder(request);

		messageSender.sendCreateOrderMessage(message);
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
		int orderCanceled = orderMapper.cancelOrder(id);
		if (checkStatus(orderCanceled, id)) {
			messageSender.sendReleaseMessage(id);
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
