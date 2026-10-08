package com.example.order.constants;

public final class RabbitConstants {

	public static final String SAGA_EXCHANGE = "saga.exchange";

	public static final String ORDER_QUEUE = "order.queue";

	public static final String ORDER_CREATED = "order.created";

	public static final String INVENTORY_REJECTED = "inventory.rejected";

	public static final String INVENTORY_RELEASE = "inventory.release";

	public static final String PAYMENT_COMPLETED = "payment.completed";

	public static final String PAYMENT_FAILED = "payment.failed";

	public static final String PAYMENT_REFUND = "payment.refund";

	private RabbitConstants() {
	}
}