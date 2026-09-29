package com.example.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	 private Long id;
	 private Long orderId;
	 private String sku;
	 private Integer qty;
}
