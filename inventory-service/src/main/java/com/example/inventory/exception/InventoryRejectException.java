package com.example.inventory.exception;

public class InventoryRejectException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InventoryRejectException(String message) {
		super(message);
	}

}
