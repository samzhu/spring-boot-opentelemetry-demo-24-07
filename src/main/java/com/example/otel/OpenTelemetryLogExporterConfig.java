package com.example.otel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProviderBuilder;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;

@Configuration
@ConditionalOnProperty(name = "otel.logs.exporter.enabled", havingValue = "true")
public class OpenTelemetryLogExporterConfig {

        private static final Logger log = LoggerFactory.getLogger(OpenTelemetryLogExporterConfig.class);

        @Value("${otel.logs.exporter.endpoint:http://127.0.0.1:4317}")
        private String logsExporterEndpoint;

        @Bean
        OpenTelemetry openTelemetry(
                        final SdkLoggerProvider sdkLoggerProvider,
                        final SdkTracerProvider sdkTracerProvider,
                        final ContextPropagators contextPropagators) {
                final OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                                .setLoggerProvider(sdkLoggerProvider)
                                .setTracerProvider(sdkTracerProvider)
                                .setPropagators(contextPropagators)
                                .build();
                OpenTelemetryAppender.install(openTelemetrySdk);
                return openTelemetrySdk;
        }

        @Bean
        SdkLoggerProvider otelSdkLoggerProvider(final Environment environment,
                        final ObjectProvider<LogRecordProcessor> logRecordProcessors) {
                final String applicationName = environment.getProperty("spring.application.name", "application");
                final Resource resource = Resource
                                .create(Attributes.of(AttributeKey.stringKey("service.name"), applicationName));
                final SdkLoggerProviderBuilder builder = SdkLoggerProvider.builder()
                                .setResource(Resource.getDefault().merge(resource));
                logRecordProcessors.orderedStream().forEach(builder::addLogRecordProcessor);
                return builder.build();
        }

        @Bean
        LogRecordProcessor otlpLogExporter() {
                return BatchLogRecordProcessor
                                .builder(OtlpGrpcLogRecordExporter.builder()
                                                .setEndpoint(logsExporterEndpoint)
                                                .build())
                                .build();
        }
}