package com.example.otel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.micrometer.observation.annotation.Observed;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    // Method to create an order
    @Observed(name = "create.order", contextualName = "creating-order")
    public String createOrder(String customerId) {
        log.info("Creating order for customer: {}", customerId);
        simulateWork(500); // Simulate the order creation process
        return "ORD-" + System.currentTimeMillis(); // Return order ID
    }

    // Method to get order status
    @Observed(name = "get.order.status", contextualName = "getting-order-status")
    public String getOrderStatus(String orderId) {
        log.info("Checking status for order: {}", orderId);
        simulateWork(200); // Simulate status checking process
        return "Status for order " + orderId + ": Processing";
    }

    // Method to process order asynchronously
    @Async // Mark this method to be executed asynchronously
    @Observed(name = "process.order.async", contextualName = "processing-order-async")
    public void processOrderAsync(String orderId) {
        log.info("Start processing order asynchronously: {}", orderId);
        simulateWork(2000); // Simulate a long-running processing task
        log.info("Finished processing order: {}", orderId);
    }

    // Private method to simulate workload
    private void simulateWork(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Work simulation interrupted", e);
        }
    }
}
