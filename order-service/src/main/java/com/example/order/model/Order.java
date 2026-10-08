package com.example.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.order.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	private Long id;
	private BigDecimal amount;
	private OrderStatus status;
	private List<OrderItem> items;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
