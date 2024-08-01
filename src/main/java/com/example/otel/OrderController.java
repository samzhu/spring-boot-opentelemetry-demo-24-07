package com.example.otel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.tracing.annotation.NewSpan;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @NewSpan("create-order")
    public String createOrder(@RequestParam String customerId) {
        log.info("Received order creation request for customer: {}", customerId);
        String orderId = orderService.createOrder(customerId);
        orderService.processOrderAsync(orderId);
        return "Order created and processing started for ID: " + orderId;
    }

    @GetMapping("/{orderId}")
    @NewSpan("get-order-status")
    public String getOrderStatus(@PathVariable String orderId) {
        log.info("Checking status for order ID: {}", orderId);
        return orderService.getOrderStatus(orderId);
    }
}