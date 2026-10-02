package com.example.inventory.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.inventory.dto.InventoryRejectMessage;
import com.example.inventory.dto.InventoryReleaseMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

	private final RabbitTemplate rabbitTemplate;

	@Override
	public void sendReserveMessage(Long id) {
		InventoryReleaseMessage message = new InventoryReleaseMessage(id);
		rabbitTemplate.convertAndSend("saga.exchange", "inventory.reserved", message);
	}

	@Override
	public void sendRejectMessage(Long id, String Reason) {
		InventoryRejectMessage message = new InventoryRejectMessage(id, Reason);
		rabbitTemplate.convertAndSend("saga.exchange", "inventory.rejected", message);

	}

}
