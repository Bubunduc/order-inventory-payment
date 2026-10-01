package com.example.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedMessage(Long orderId,List<OrderItemRequest> items, BigDecimal amount) {

}
