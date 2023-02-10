package com.epam.learn.distance.handler;

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

    private final KafkaTemplate<String, BigDecimal> firstTemplate;
    private final KafkaTemplate<String, BigDecimal> secondTemplate;
    private final KafkaTemplate<String, BigDecimal> thirdTemplate;

    @Value("${spring.kafka.distance-topic}")
    private String distanceTopic;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.signal-topic}", clientIdPrefix = "first-signal-handler", groupId = "signal-handlers")
    public void handleAsFirst(ConsumerRecord<String, VehicleSignal> signalRecord) {
        firstTemplate.send(distanceTopic, signalRecord.key(), calculateDistance(signalRecord));
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.signal-topic}", clientIdPrefix = "second-signal-handler", groupId = "signal-handlers")
    public void handleAsSecond(ConsumerRecord<String, VehicleSignal> signalRecord) {
        secondTemplate.send(distanceTopic, signalRecord.key(), calculateDistance(signalRecord));
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.signal-topic}", clientIdPrefix = "third-signal-handler", groupId = "signal-handlers")
    public void handleAsThird(ConsumerRecord<String, VehicleSignal> signalRecord) {
        thirdTemplate.send(distanceTopic, signalRecord.key(), calculateDistance(signalRecord));
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
