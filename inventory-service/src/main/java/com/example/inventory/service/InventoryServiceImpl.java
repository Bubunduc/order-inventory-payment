package com.example.inventory.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
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

	private final MessageSender sender;

	@Override
	@Transactional
	public void reserve(OrderCreatedMessage orderCreatedMessage) throws InventoryRejectException {
		Long orderId = orderCreatedMessage.orderId();
		String errMessage;

		if (processedOrderMapper.findById(orderId) != null) {
			errMessage = "Заказ с " + orderId + " уже существует в системе";
			sender.sendRejectMessage(orderId, errMessage);
			throw new InventoryRejectException(errMessage);
		}

		Map<String, Integer> skus = orderCreatedMessage.items().stream()
				.collect(Collectors.toMap(x -> x.sku(), x -> x.qty()));

		List<Stock> stocks = stockMapper.findAllBySku(skus.keySet());

		Map<String, Stock> stockMap = stocks.
				stream().
				collect(Collectors.toMap(x -> x.getSku(), x -> x));

		List<String> notFoundSkus = skus.
				keySet().
				stream().
				filter(sku -> !stockMap.containsKey(sku)).toList();

		if (!notFoundSkus.isEmpty()) {
			errMessage = "Не найдены SKU: " + notFoundSkus;
			sender.sendRejectMessage(orderId, errMessage);
			throw new InventoryRejectException(errMessage);
		}

		for (Map.Entry<String, Integer> item : skus.entrySet()) {

			String sku = item.getKey();
			Integer qty = item.getValue();

			Stock stock = stockMap.get(sku);

			if (stock.getAvailableQty() < qty) {
				errMessage = "Недостаточно товара: " + sku;
				sender.sendRejectMessage(orderId, "Недостаточно товара: " + sku);
				throw new InventoryRejectException(errMessage);
			}
			
			int updatedRows = stockMapper.reserve(sku, qty);
            if (updatedRows == 0) {
            	errMessage = "Недостаточно товара (конкурентная транзакция): " + sku;
            	throw new InventoryRejectException(errMessage);
            }
			reservedItemMapper.insert(orderId, sku, qty);
		}
		try {
            processedOrderMapper.insert(orderId);
        } catch (DuplicateKeyException e) {
             log.warn("Параллельная обработка заказа {}", orderId);
             return;
        }
		sender.sendReserveMessage(orderId);
	}

	@Override
	@Transactional
	public void release(InventoryReleaseMessage message) {
	    Long orderId = message.orderId();
	    
	    List<ReservedItem> reservedItems = reservedItemMapper.findAllById(orderId);
	    if (reservedItems.isEmpty()) {
	        System.out.println("Резервы для заказа " +orderId + "не найдены или уже были отменены.");
	        return;
	    }
	    for (ReservedItem item : reservedItems) {
	        stockMapper.release(item.getSku(), item.getQty());
	    }
	    reservedItemMapper.deleteByOrderId(orderId);
	}
	
}
