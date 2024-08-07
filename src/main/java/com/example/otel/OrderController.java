package com.example.otel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.observation.annotation.Observed;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Observed(name = "order.create.request", contextualName = "create-order-request")
    public String createOrder(@RequestParam String customerId) {
        log.info("Received order creation request for customer: {}", customerId);
        String orderId = orderService.createOrder(customerId);
        orderService.processOrderAsync(orderId);
        return orderService.getOrderStatus(orderId);
    }
}