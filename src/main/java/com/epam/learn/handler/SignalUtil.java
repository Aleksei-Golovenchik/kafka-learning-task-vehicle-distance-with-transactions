package com.epam.learn.handler;

import com.epam.learn.dto.SignalDistanceEntry;
import com.epam.learn.dto.VehicleSignal;

import java.math.BigDecimal;
import java.math.MathContext;

public final class SignalUtil {
    private SignalUtil() {}

    public static void evaluateDistanceAndUpdateLastCoordinates(SignalDistanceEntry signalDistanceEntry, VehicleSignal signal) {
        BigDecimal distance = signalDistanceEntry.getDistance();
        BigDecimal lastX = signalDistanceEntry.getLastX();
        BigDecimal lastY = signalDistanceEntry.getLastY();
        distance = distance.add((lastX.subtract(signal.getX()).pow(2).add(lastY.subtract(signal.getY()).pow(2))).sqrt(MathContext.DECIMAL32));
        signalDistanceEntry.setDistance(distance);
        signalDistanceEntry.setLastX(signal.getX());
        signalDistanceEntry.setLastY(signal.getY());
    }
}
