package com.example.payment.constants;

public final class RabbitConstants {

	public static final String SAGA_EXCHANGE = "saga.exchange";

	public static final String PAYMENT_QUEUE = "payment.queue";

	public static final String INVENTORY_RESERVED = "inventory.reserved";

	public static final String PAYMENT_COMPLETED = "payment.completed";

	public static final String PAYMENT_FAILED = "payment.failed";

	public static final String PAYMENT_REFUND = "payment.refund";

	private RabbitConstants() {
	}
}
