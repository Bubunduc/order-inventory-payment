package com.example.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.order.constants.RabbitConstants;

@Configuration
public class RabbitConfig {
	@Bean
	public TopicExchange sagaExchange() {
		return new TopicExchange(RabbitConstants.SAGA_EXCHANGE);
	}

	@Bean
	public Queue orderQueue() {
		return new Queue(RabbitConstants.ORDER_QUEUE, true);
	}

	@Bean
	public Binding paymentCompleteBinding(Queue orderQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(orderQueue).to(sagaExchange).with(RabbitConstants.PAYMENT_COMPLETED);
	}

	@Bean
	public Binding paymentFailBinding(Queue orderQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(orderQueue).to(sagaExchange).with(RabbitConstants.PAYMENT_FAILED);
	}

	@Bean
	public Binding inventoryRejectBinding(Queue orderQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(orderQueue).to(sagaExchange).with(RabbitConstants.INVENTORY_REJECTED);
	}
}
