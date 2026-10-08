package com.example.payment.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.payment.constants.RabbitConstants;

@Configuration
public class RabbitConfig {
	@Bean
	public TopicExchange sagaExchange() {
		return new TopicExchange(RabbitConstants.SAGA_EXCHANGE);
	}

	@Bean
	public Queue paymentQueue() {
		return new Queue(RabbitConstants.PAYMENT_QUEUE, true);
	}

	@Bean
	public Binding inventoryReservedBinding(Queue paymentQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(paymentQueue).to(sagaExchange).with(RabbitConstants.INVENTORY_RESERVED);
	}
	
	@Bean
	public Binding paymentRefundBinding(Queue paymentQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(paymentQueue).to(sagaExchange).with(RabbitConstants.PAYMENT_REFUND);
	}

}
