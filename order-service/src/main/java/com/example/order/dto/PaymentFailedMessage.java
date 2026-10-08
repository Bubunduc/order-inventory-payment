package com.example.order.dto;

public record PaymentFailedMessage(Long orderId, String reason) {

}
