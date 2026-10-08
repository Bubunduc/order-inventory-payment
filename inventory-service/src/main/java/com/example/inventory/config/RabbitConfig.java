package com.example.inventory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.inventory.constants.RabbitConstants;

@Configuration
public class RabbitConfig {
	@Bean
	public TopicExchange sagaExchange() {
		return new TopicExchange(RabbitConstants.SAGA_EXCHANGE);
	}

	@Bean
	public Queue inventoryQueue() {
		return new Queue(RabbitConstants.INVENTORY_QUEUE, true);
	}

	@Bean
	public Binding inventoryOrderCreatedBinding(Queue inventoryQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(inventoryQueue).to(sagaExchange).with(RabbitConstants.ORDER_CREATED);
	}

	@Bean
	public Binding inventoryReleaseBinding(Queue inventoryQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(inventoryQueue).to(sagaExchange).with(RabbitConstants.INVENTORY_RELEASE);
	}
}
