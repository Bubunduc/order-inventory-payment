package com.example.inventory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	@Bean
	public TopicExchange sagaExchange() {
		return new TopicExchange("saga.exchange");
	}

	@Bean
	public Queue inventoryQueue() {
		return new Queue("inventory.queue", true);
	}

	@Bean
	public Binding inventoryOrderCreatedBinding(Queue inventoryQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(inventoryQueue).to(sagaExchange).with("order.created");
	}

	@Bean
	public Binding inventoryReleaseBinding(Queue inventoryQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(inventoryQueue).to(sagaExchange).with("inventory.release");
	}
}
