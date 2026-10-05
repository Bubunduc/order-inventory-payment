package com.example.inventory.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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

	@RabbitListener(queues = "inventory.queue")
	public void listen(Message message) throws Exception {
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

	private void handleOrderCreated(Message message) throws Exception {
		OrderCreatedMessage orderCreatedMessage = objectMapper.
				readValue(message.getBody(), OrderCreatedMessage.class);
		try {
			boolean reserved = inventoryService.reserve(orderCreatedMessage);

			if (!reserved) {
				return;
			}

			sender.sendReserveMessage(orderCreatedMessage.orderId());

		} catch (InventoryRejectException e) {
			log.warn("Заказ {} отклонён: {}", orderCreatedMessage.orderId(), e.getMessage());
			sender.sendRejectMessage(orderCreatedMessage.orderId(), e.getMessage());
		}
	}

	private void handleInventoryRelease(Message message) throws Exception {
		InventoryReleaseMessage releaseMessage = objectMapper.readValue(message.getBody(),
				InventoryReleaseMessage.class);
		
		inventoryService.release(releaseMessage);
	}
}