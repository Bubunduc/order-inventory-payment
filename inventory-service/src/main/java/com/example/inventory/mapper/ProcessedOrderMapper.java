package com.example.inventory.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProcessedOrderMapper {
	int insertIfAbsent(@Param("orderId") Long orderId);
}
