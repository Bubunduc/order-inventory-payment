package com.example.inventory.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;

@Component
public class OrderClient {

	@Value("${order.service.url}")
	private String orderServiceUrl;

	private RestClient restClient;

	@PostConstruct
	private void init() {
		restClient = RestClient.builder().baseUrl(orderServiceUrl).build();
	}

	public void awaitingPaymentRequest(Long orderId) {
		restClient.
		patch().
		uri("/orders/{orderId}/awaiting-payment", orderId).
		retrieve().
		toBodilessEntity();
	}
}
