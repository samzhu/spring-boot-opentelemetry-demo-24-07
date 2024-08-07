package com.example.otel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

        private static final Logger log = LoggerFactory.getLogger(OrderController.class);

        // https://docs.spring.io/spring-framework/reference/integration/observability.html#observability.application-events
        @Bean(name = "propagatingContextExecutor")
        public TaskExecutor propagatingContextExecutor() {
                SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
                // decorate task execution with a decorator that supports context propagation
                taskExecutor.setTaskDecorator(new ContextPropagatingTaskDecorator());
                return taskExecutor;
        }

}
