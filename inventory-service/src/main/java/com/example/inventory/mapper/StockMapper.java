package com.example.inventory.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.inventory.model.Stock;

@Mapper
public interface StockMapper {
	List<Stock> findAllBySku(@Param("skus") Set<String> set);

	int reserve(@Param("sku") String sku, @Param("qty") Integer qty);
	
	void release(@Param("sku") String sku, @Param("qty") Integer qty);
}
