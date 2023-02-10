package com.epam.learn.receiver.service;

import com.epam.learn.dto.VehicleSignal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignalPublisher {



    @Value("${spring.kafka.signal-topic}")
    private String topic;
    private final KafkaTemplate<String, VehicleSignal> kafkaTemplate;

    public void publish(String id, VehicleSignal signal) {
        ListenableFuture<SendResult<String, VehicleSignal>> future = kafkaTemplate.send(topic, id, signal);
        future.addCallback(
                result ->log.info("Successfully sent signal {} for vehicle: {}", signal, id),
                err -> log.error("unable to send: {}", err.getMessage()));
    }

}
