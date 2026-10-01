package com.example.order.exception;

public class DuplicateSkuException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateSkuException() {
		super("Присутствуют присутствуют повторяющиеся sku");
	}
}