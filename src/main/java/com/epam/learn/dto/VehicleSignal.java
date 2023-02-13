package com.epam.learn.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class VehicleSignal {

    @NotNull
    @DecimalMax("1000")
    @DecimalMin("-1000")
    private BigDecimal x;

    @NotNull
    @DecimalMax("1000")
    @DecimalMin("-1000")
    private BigDecimal y;
}
