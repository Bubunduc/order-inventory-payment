package com.example.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(List<OrderItemRequest> items, BigDecimal amount) {
}
