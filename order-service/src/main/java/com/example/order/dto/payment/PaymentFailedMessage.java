package com.example.order.dto.payment;

public record PaymentFailedMessage(Long orderId, String reason) {

}
