package com.epam.learn.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SignalDistanceEntry {
    private BigDecimal lastX;
    private BigDecimal lastY;
    private BigDecimal distance;


    public static SignalDistanceEntry createWithZeroDistance(VehicleSignal signal) {
        SignalDistanceEntry signalDistanceEntry = new SignalDistanceEntry();
        signalDistanceEntry.setLastX(signal.getX());
        signalDistanceEntry.setLastY(signal.getY());
        signalDistanceEntry.setDistance(BigDecimal.ZERO);
        return  signalDistanceEntry;
    }
}
