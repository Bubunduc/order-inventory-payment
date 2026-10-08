package com.example.order.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
		@NotEmpty
		@Valid
		List<OrderItemRequest> items, 
		
		@NotNull
		@Positive
		BigDecimal amount) {
}
