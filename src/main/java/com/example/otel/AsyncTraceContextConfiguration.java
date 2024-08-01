package com.example.otel;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ContextSnapshotFactory;

@Configuration(proxyBeanMethods = false)
public class AsyncTraceContextConfiguration implements AsyncConfigurer {

  public AsyncTraceContextConfiguration(ThreadPoolTaskExecutor taskExecutor) {
    this.taskExecutor = taskExecutor;
  }

  // 注入ThreadPoolTaskExecutor
  private final ThreadPoolTaskExecutor taskExecutor;

  @Override
  public Executor getAsyncExecutor() {
    // 建立ContextSnapshotFactory實例
    ContextSnapshotFactory contextSnapshotFactory = ContextSnapshotFactory.builder()
        .contextRegistry(ContextRegistry.getInstance())
        .build();
    // 包裝原始執行器以支持上下文傳播
    return ContextExecutorService.wrap(
        taskExecutor.getThreadPoolExecutor(),
        contextSnapshotFactory::captureAll);
  }
}