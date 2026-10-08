package com.example.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
	private String sku;
	private Integer availableQty;
	private Integer reservedQty;
	@Override
	public String toString() {
		return "Stock [sku=" + sku + ", availableQty=" + availableQty + ", reservedQty=" + reservedQty + "]";
	}
		
}

