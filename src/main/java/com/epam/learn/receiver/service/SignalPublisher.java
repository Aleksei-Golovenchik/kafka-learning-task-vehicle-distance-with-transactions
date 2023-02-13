package com.epam.learn.receiver.service;

import com.epam.learn.dto.VehicleSignal;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class SignalPublisher {



    @Value("${spring.kafka.signal-topic}")
    private String topic;

    private  final Producer <String, VehicleSignal> producer;

    public SignalPublisher(ProducerFactory<String, VehicleSignal> factory) {
        this.producer = factory.createNonTransactionalProducer();
    }

    public void publish(String id, VehicleSignal signal) {
        Future<RecordMetadata> future = producer.send(new ProducerRecord<>(topic, id, signal));
        try {
            future.get();
            log.info("Successfully sent signal {} for vehicle: {}", signal, id);
        } catch (InterruptedException | ExecutionException e) {
            log.error("unable to send: {} because of: {}", id, e.getMessage());
        }
    }

}
