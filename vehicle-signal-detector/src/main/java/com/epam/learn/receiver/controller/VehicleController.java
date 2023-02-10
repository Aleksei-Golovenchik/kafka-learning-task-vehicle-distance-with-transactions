package com.epam.learn.receiver.controller;

import com.epam.learn.dto.VehicleSignal;
import com.epam.learn.receiver.service.SignalPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Digits;

@RestController
@RequestMapping("/api/vehicles")
@Validated
@RequiredArgsConstructor
public class VehicleController {

    private final SignalPublisher signalPublisher;

    @PostMapping("/{id}/signal")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void acceptSignal(@PathVariable @Digits(integer = 5, fraction = 0) String id, @RequestBody @Valid VehicleSignal signal) {
        signalPublisher.publish(id, signal);
    }
}
