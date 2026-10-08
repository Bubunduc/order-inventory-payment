package com.example.payment.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.payment.enums.PaymentStatus;

@Mapper
public interface PaymentMapper {
	int insertIfAbsent(
			@Param("orderId") Long orderId,
			@Param("amount") BigDecimal amount,
			@Param("status") PaymentStatus status
			);

	int completePayment(@Param("orderId") Long orderId);

	int failPayment(@Param("orderId") Long orderId);

	int refund(@Param("orderId") Long orderId);
}
