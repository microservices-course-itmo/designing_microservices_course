package com.microservices.taskcoordinator.metrics;

import io.micrometer.elastic.ElasticMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonTagsConfiguration {

    @Value("${grafana.instance.name}")
    private String instanceName;

    @Bean
    MeterRegistryCustomizer<ElasticMeterRegistry> commonTags() {
        return registry -> registry.config().commonTags("instance", instanceName);
    }
}
