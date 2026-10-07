package com.example.payment.dto;

import java.math.BigDecimal;

public record InventoryReserveMessage(Long orderId,BigDecimal amount) {
}
