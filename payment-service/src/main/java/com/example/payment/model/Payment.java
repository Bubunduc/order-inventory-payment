package com.example.payment.model;

import java.math.BigDecimal;

import com.example.payment.emuns.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment {
	private Long id;
	private Long orderId;
	private BigDecimal amount;
	PaymentStatus status;

	@Override
	public String toString() {
		return "Payment [id=" + id + ", orderId=" + orderId + ", amount=" + amount + ", status=" + status + "]";
	}
}
