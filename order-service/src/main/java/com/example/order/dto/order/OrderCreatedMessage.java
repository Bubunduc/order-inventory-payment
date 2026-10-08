package com.example.order.dto.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedMessage(Long orderId, List<OrderCreatedMessageItem> items, BigDecimal amount) {

}
