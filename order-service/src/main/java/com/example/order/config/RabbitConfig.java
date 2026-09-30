package com.example.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Bean
	public MessageConverter jsonMessageConverter() {

		return new JacksonJsonMessageConverter();
	}

	@Bean
	public TopicExchange sagaExchange() {
		return new TopicExchange("saga.exchange");
	}

	@Bean
	public Queue inventoryQueue() {
		return new Queue("order.queue", true);
	}

	@Bean
	public Binding paymentCompleteBinding(Queue inventoryQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(inventoryQueue).to(sagaExchange).with("payment.completed");
	}

	@Bean
	public Binding paymentFailBinding(Queue inventoryQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(inventoryQueue).to(sagaExchange).with("payment.failed");
	}

	@Bean
	public Binding inventoryRejectBinding(Queue inventoryQueue, TopicExchange sagaExchange) {
		return BindingBuilder.bind(inventoryQueue).to(sagaExchange).with("inventory.rejected");
	}
}