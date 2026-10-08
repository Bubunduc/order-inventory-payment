package com.example.inventory.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;

import com.example.inventory.client.OrderClient;
import com.example.inventory.dto.InventoryReleaseMessage;
import com.example.inventory.dto.OrderCreatedMessage;
import com.example.inventory.exception.InventoryRejectException;
import com.example.inventory.service.InventoryService;
import com.example.inventory.service.MessageSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryListener {

	private final InventoryService inventoryService;
	private final MessageSender sender;
	private final ObjectMapper objectMapper;
	private final OrderClient orderClient;

	@RabbitListener(queues = "inventory.queue")
	public void listen(Message message) {
		String routingKey = message.getMessageProperties().getReceivedRoutingKey();

		switch (routingKey) {

		case "order.created":
			handleOrderCreated(message);
			break;

		case "inventory.release":
			handleInventoryRelease(message);
			break;

		default:
			log.warn("Получено сообщение с неизвестным routing key: {}", routingKey);
		}
	}

	private void handleOrderCreated(Message message) {
		OrderCreatedMessage orderCreatedMessage = objectMapper.readValue(message.getBody(), OrderCreatedMessage.class);
		try {
			boolean reserved = inventoryService.reserve(orderCreatedMessage);

			if (!reserved) {
				return;
			}
			orderClient.awaitingPaymentRequest(orderCreatedMessage.orderId());
			sender.sendReserveMessage(orderCreatedMessage.orderId(),orderCreatedMessage.amount());

		} catch (InventoryRejectException e) {
			log.warn("Заказ {} отклонён: {}", orderCreatedMessage.orderId(), e.getMessage());
			sender.sendRejectMessage(orderCreatedMessage.orderId(), e.getMessage());
		} catch (DataAccessException e) {
			log.error("Ошибка бд при обработке заказа {}", orderCreatedMessage.orderId(), e);
			sender.sendRejectMessage(orderCreatedMessage.orderId(), "Ошибка при работе с базой данных Inventory Service");
		} catch (TransactionException e) {
			log.error("Не удалось выполнить транзакцию для заказа {}", orderCreatedMessage.orderId(), e);
			sender.sendRejectMessage(orderCreatedMessage.orderId(), "Ошибка при работе с базой данных Inventory Service");
		}
	}

	private void handleInventoryRelease(Message message) {
		InventoryReleaseMessage releaseMessage = objectMapper.readValue(message.getBody(),
				InventoryReleaseMessage.class);
		try {
			inventoryService.release(releaseMessage);
		} catch (DataAccessException e) {
			log.error("Ошибка бд при обработке заказа {}", releaseMessage.orderId(), e);
			throw e;
		} catch (TransactionException e) {
			log.error("Не удалось выполнить транзакцию для заказа {}", releaseMessage.orderId(), e);
			throw e;
		}
	}
}
