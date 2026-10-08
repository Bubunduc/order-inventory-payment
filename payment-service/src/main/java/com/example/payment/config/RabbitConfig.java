package com.example.payment.config;

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
	public Queue paymentQueue() {
		return new Queue("payment.queue", true);
	}

	@Bean
	public Binding inventoryReservedBinding(Queue paymentQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(paymentQueue).to(sagaExchange).with("inventory.reserved");
	}
	
	@Bean
	public Binding paymentRefundBinding(Queue paymentQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(paymentQueue).to(sagaExchange).with("payment.refund");
	}

}
