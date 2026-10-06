package com.example.payment.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.payment.emuns.PaymentStatus;

@Mapper
public interface PaymentMapper {
	int insertIfAbsent(
			@Param("order_id") Long orderId, 
			@Param("amount") BigDecimal amount,
			@Param("status") PaymentStatus status
			);

	int updateStatus(@Param("id") Long id, @Param("status") PaymentStatus status);
}
