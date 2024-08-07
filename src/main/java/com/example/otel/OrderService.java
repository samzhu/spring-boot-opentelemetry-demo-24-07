package com.example.otel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Tracer;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private Tracer tracer;

    @Observed(name = "order.create", contextualName = "create-order")
    public String createOrder(String customerId) {
        log.info("Creating order for customer: {}, traceId: {}", customerId, tracer.currentTraceContext().context().traceId());
        simulateWork(500);
        return "ORD-" + System.currentTimeMillis();
    }

    @Async
    @Observed(name = "order.process.async", contextualName = "process-order-async")
    public void processOrderAsync(String orderId) {
        log.info("Start processing order asynchronously: {}, traceId: {}", orderId, tracer.currentTraceContext().context().traceId());
        simulateWork(2000);
        log.info("Finished processing order: {}", orderId);
    }

    @Observed(name = "order.status", contextualName = "get-order-status")
    public String getOrderStatus(String orderId) {
        log.info("Checking status for order: {}", orderId);
        simulateWork(200);
        return String.format("Status for order %s: Processing", orderId);
    }

    private void simulateWork(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Work simulation interrupted", e);
        }
    }
}
