package com.example.inventory.dto;

public record InventoryRejectMessage(Long orderId, String reason) {

}
