package com.example.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.order.model.OrderItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
		@NotBlank
		String sku,
		
		@NotNull
		@Positive
		Integer qty) {

	public OrderItem toEntity() {
		return new OrderItem(this.sku, this.qty);
	}

	public static List<OrderItem> toEntityList(List<OrderItemRequest> requests) {
		if (requests == null) {
			return List.of();
		}
		return requests.stream().map(OrderItemRequest::toEntity).collect(Collectors.toList());
	}
	public static OrderItemRequest fromEntity(OrderItem item) {
        if (item == null) {
            return null;
        }
        return new OrderItemRequest(item.getSku(), item.getQty());
    }
}
