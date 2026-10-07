package com.example.payment.dto;

public record PaymentFailedMessage(Long orderId, String reason) {

}
