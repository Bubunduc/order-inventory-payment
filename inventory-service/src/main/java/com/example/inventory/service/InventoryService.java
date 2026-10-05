package com.example.inventory.service;

import com.example.inventory.dto.InventoryReleaseMessage;
import com.example.inventory.dto.OrderCreatedMessage;
import com.example.inventory.exception.InventoryRejectException;

public interface InventoryService {

	boolean reserve(OrderCreatedMessage orderCreatedMessage) throws InventoryRejectException;

	void release(InventoryReleaseMessage orderCreatedMessage);

}
