package com.epam.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.math.BigDecimal;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, BigDecimal> firstTemplate(ProducerFactory<String, BigDecimal> factory) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(factory.getConfigurationProperties()));
    }
    @Bean
    public KafkaTemplate<String, BigDecimal> secondTemplate(ProducerFactory<String, BigDecimal> factory) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(factory.getConfigurationProperties()));
    }
    @Bean
    public KafkaTemplate<String, BigDecimal> thirdTemplate(ProducerFactory<String, BigDecimal> factory) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(factory.getConfigurationProperties()));
    }
}
