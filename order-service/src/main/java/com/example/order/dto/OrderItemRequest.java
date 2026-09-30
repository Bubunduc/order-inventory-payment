package com.example.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.order.model.OrderItem;

public record OrderItemRequest(String sku, int quantity) {

	public OrderItem toEntity() {
		return new OrderItem(this.sku, this.quantity);
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
