package com.example.inventory.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.inventory.dto.InventoryReleaseMessage;
import com.example.inventory.dto.OrderCreatedMessage;
import com.example.inventory.exception.InventoryRejectException;
import com.example.inventory.service.InventoryService;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class InventoryListener {

	private final InventoryService inventoryService;
	private final ObjectMapper objectMapper;

	@RabbitListener(queues = "inventory.queue")
	public void listen(Message message) throws Exception {

		String routingKey = message.getMessageProperties().getReceivedRoutingKey();

		if ("order.created".equals(routingKey)) {
			OrderCreatedMessage orderCreatedMessage = objectMapper.readValue(message.getBody(),
					OrderCreatedMessage.class);
			try {
				inventoryService.reserve(orderCreatedMessage);
			} catch (InventoryRejectException e) {
				System.out.println(e.getMessage());
			}
		} else if ("inventory.release".equals(routingKey)) {
			InventoryReleaseMessage orderReleasedMessage = objectMapper.readValue(message.getBody(),
					InventoryReleaseMessage.class);

			inventoryService.release(orderReleasedMessage);
		}
	}
}
