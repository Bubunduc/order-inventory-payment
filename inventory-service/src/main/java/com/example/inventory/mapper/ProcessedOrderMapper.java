package com.example.inventory.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.inventory.model.ProcessedOrder;

@Mapper
public interface ProcessedOrderMapper {
	ProcessedOrder findById(@Param("orderId") Long orderId);

	void insert(Long orderId);
}
