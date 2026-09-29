package com.example.order.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.GetOrderRequest;
import com.example.order.service.OrderService;


@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService orderService;
	
	public OrderController(RabbitTemplate rabbitTemplate,OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest request) {
		orderService.createOrder(request);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{id}")
	public GetOrderRequest getOrder(@PathVariable Long id){
		return orderService.getOrderById(id) ;
	}
}
