package com.epam.learn.handler;

import com.epam.learn.dto.SignalDistanceEntry;
import com.epam.learn.dto.VehicleSignal;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SignalHandlingService {

    private static final Cache<String, SignalDistanceEntry> cache = CacheBuilder.newBuilder().concurrencyLevel(3).build();

    private final KafkaTemplate<String, BigDecimal> kafkaTemplate;

    @Value("${spring.kafka.distance-topic}")
    private String distanceTopic;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.signal-topic}", groupId = "signal-handlers", concurrency = "3")
    public void handleInputSignal(ConsumerRecord<String, VehicleSignal> signalRecord) {
        kafkaTemplate.send(distanceTopic, signalRecord.key(), calculateDistance(signalRecord));
    }

    private BigDecimal calculateDistance(ConsumerRecord<String, VehicleSignal> signalRecord) {
        SignalDistanceEntry signalDistanceEntry = cache.getIfPresent(signalRecord.key());
        if (signalDistanceEntry == null) {
            signalDistanceEntry = SignalDistanceEntry.createWithZeroDistance(signalRecord.value());
        }
        SignalUtil.evaluateDistanceAndUpdateLastCoordinates(signalDistanceEntry, signalRecord.value());
        cache.put(signalRecord.key(), signalDistanceEntry);
        return signalDistanceEntry.getDistance();

    }
}
