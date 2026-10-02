package com.example.inventory.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.inventory.model.ProcessedOrder;

@Mapper
public interface ProcessedOrderMapper {
	ProcessedOrder findById(Long orderId);

	void insert(Long orderId);
}
