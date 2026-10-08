package com.example.inventory.constants;

public final class RabbitConstants {

	public static final String SAGA_EXCHANGE = "saga.exchange";

	public static final String INVENTORY_QUEUE = "inventory.queue";

	public static final String ORDER_CREATED = "order.created";

	public static final String INVENTORY_RESERVED = "inventory.reserved";

	public static final String INVENTORY_REJECTED = "inventory.rejected";

	public static final String INVENTORY_RELEASE = "inventory.release";
	
	private RabbitConstants() {
	}
}