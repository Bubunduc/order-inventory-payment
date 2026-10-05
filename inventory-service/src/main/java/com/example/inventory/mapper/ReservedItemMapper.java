package com.example.inventory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.inventory.model.ReservedItem;

@Mapper
public interface ReservedItemMapper {

	List<ReservedItem> findAllById(@Param("orderId") Long orderId);

	void insert(@Param("orderId") Long orderId, @Param("sku") String sku, @Param("qty") Integer qty);
	
	void deleteByOrderId(@Param("orderId") Long orderId);
}
