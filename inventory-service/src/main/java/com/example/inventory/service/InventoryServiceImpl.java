package com.example.inventory.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.dto.InventoryReleaseMessage;
import com.example.inventory.dto.OrderCreatedMessage;
import com.example.inventory.exception.InventoryRejectException;
import com.example.inventory.mapper.ProcessedOrderMapper;
import com.example.inventory.mapper.ReservedItemMapper;
import com.example.inventory.mapper.StockMapper;
import com.example.inventory.model.ReservedItem;
import com.example.inventory.model.Stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final StockMapper stockMapper;
	private final ProcessedOrderMapper processedOrderMapper;
	private final ReservedItemMapper reservedItemMapper;

	@Override
	@Transactional
	public boolean reserve(OrderCreatedMessage orderCreatedMessage) {
		Long orderId = orderCreatedMessage.orderId();
		int inserted = processedOrderMapper.insertIfAbsent(orderId);

		if (inserted == 0) {
			log.info("Заказ с id {} уже был обработан", orderId);
			return false;
		}

		Map<String, Integer> skus = orderCreatedMessage
				.items()
				.stream()
				.collect(Collectors.toMap(item -> item.sku(), item -> item.qty()));

		List<Stock> stocks = stockMapper.findAllBySku(skus.keySet());

		Map<String, Stock> stockMap = stocks.
				stream().
				collect(Collectors.toMap(Stock::getSku, stock -> stock));

		List<String> notFoundSkus = skus.
				keySet().
				stream().
				filter(sku -> !stockMap.containsKey(sku)).toList();

		if (!notFoundSkus.isEmpty()) {
			throw new InventoryRejectException("Не найдены SKU: " + notFoundSkus);
		}

		for (Map.Entry<String, Integer> item : skus.entrySet()) {

			String sku = item.getKey();
			Integer qty = item.getValue();

			Stock stock = stockMap.get(sku);

			if (stock.getAvailableQty() < qty) {
				throw new InventoryRejectException("Недостаточно товара: " + sku);
			}
			int updatedRows = stockMapper.reserve(sku, qty);
			if (updatedRows == 0) {
				throw new InventoryRejectException("Недостаточно товара: " + sku);
			}
			reservedItemMapper.insert(orderId, sku, qty);
		}

		return true;
	}

	@Override
	@Transactional
	public void release(InventoryReleaseMessage message) {
		Long orderId = message.orderId();
		List<ReservedItem> reservedItems = reservedItemMapper.findAllByOrderId(orderId);

		if (reservedItems.isEmpty()) {
			log.info("Резервы для заказа {} не найдены или уже были отменены", orderId);
			return;
		}

		for (ReservedItem item : reservedItems) {
			int updatedRows = stockMapper.release(item.getSku(), item.getQty());
			if (updatedRows == 0) {
				throw new IllegalStateException("Не удалось снять резерв для SKU: " + item.getSku());
			}
		}
		reservedItemMapper.deleteByOrderId(orderId);
		log.info("Резерв заказа {} успешно снят", orderId);
	}
}