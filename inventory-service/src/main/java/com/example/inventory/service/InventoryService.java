package com.example.inventory.service;

import com.example.inventory.dto.InventoryReleaseMessage;
import com.example.inventory.dto.OrderCreatedMessage;

public interface InventoryService {

	boolean reserve(OrderCreatedMessage message);

	void release(InventoryReleaseMessage message);

}
