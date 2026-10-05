package com.example.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservedItem {
	private Long orderId;
	private String sku;
	private Integer qty;

	@Override
	public String toString() {
		return "ReservedItem [orderId=" + orderId + ", sku=" + sku + ", qty=" + qty + "]";
	}
}
