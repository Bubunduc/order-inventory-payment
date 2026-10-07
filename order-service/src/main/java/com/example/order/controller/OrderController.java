package com.example.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.GetOrderResponse;
import com.example.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequest request) {
		orderService.createOrder(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long id) {
		GetOrderResponse response = orderService.getOrderById(id);
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{orderId}/awaiting-payment")
	public void awaiting(@PathVariable Long orderId) {
		orderService.setAwaitingPaymentStatus(orderId);
	}
}
