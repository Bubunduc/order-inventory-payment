package com.example.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedMessage(
		Long orderId,
		List<OrderCreatedMessageItem> items,
		BigDecimal ammount) {

}
