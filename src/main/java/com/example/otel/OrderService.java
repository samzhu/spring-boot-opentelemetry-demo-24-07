package com.example.otel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.micrometer.tracing.annotation.NewSpan;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    // 建立訂單的方法
    @NewSpan("create-order-service") // 為此方法創建一個新的追蹤 span
    public String createOrder(String customerId) {
        log.info("Creating order for customer: {}", customerId);
        simulateWork(500); // 模擬訂單建立過程
        return "ORD-" + System.currentTimeMillis(); // 返回訂單 ID
    }

    // 取得訂單狀態的方法
    @NewSpan("get-order-status-service") // 為此方法創建一個新的追蹤 span
    public String getOrderStatus(String orderId) {
        log.info("Checking status for order: {}", orderId);
        simulateWork(200); // 模擬狀態檢查過程
        return "Status for order " + orderId + ": Processing";
    }

    // 異步處理訂單的方法
    @Async // 標記此方法為異步執行
    @NewSpan("process-order-async") // 為此異步方法創建一個新的追蹤 span
    public void processOrderAsync(String orderId) {
        log.info("Start processing order asynchronously: {}", orderId);
        simulateWork(2000); // 模擬長時間運行的處理過程
        log.info("Finished processing order: {}", orderId);
    }

    // 模擬工作負載的私有方法
    private void simulateWork(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Work simulation interrupted", e);
        }
    }
}