package com.example.order.dto.inventory;

public record InventoryRejectMessage(Long orderId, String reason) {

}
