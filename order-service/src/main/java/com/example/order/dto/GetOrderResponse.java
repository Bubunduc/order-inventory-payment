package com.example.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.order.enums.OrderStatus;
import com.example.order.model.Order;

public record GetOrderResponse(
		Long id,
		BigDecimal amount,
		OrderStatus status,
		List<OrderItemRequest> items,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {

	public static GetOrderResponse fromEntity(Order order) {
		if (order == null) {
			return null;
		}
		List<OrderItemRequest> items = order.getItems() != null
				? order.getItems().stream().map(OrderItemRequest::fromEntity).toList()
				: List.of();

		return new GetOrderResponse(
				order.getId(),
				order.getAmount(), 
				order.getStatus(), 
				items, 
				order.getCreatedAt(),
				order.getUpdatedAt());
	}
}
