package com.example.order.dto.order;

import java.math.BigDecimal;
import java.util.List;

import com.example.order.dto.request.OrderItemRequest;

public record OrderCreatedMessage(Long orderId, List<OrderItemRequest> items, BigDecimal amount) {

}
