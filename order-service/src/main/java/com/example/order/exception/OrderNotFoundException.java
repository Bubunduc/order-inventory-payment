package com.example.order.exception;

public class OrderNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(Long id) {
		super("Заказ с id " + id + " не найден");
	}
}
