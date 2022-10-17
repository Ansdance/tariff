package de.angelasensio.tariff.domain;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public enum ModuleType {

    BIKE(new BigDecimal("0.3")),
    JEWELRY(new BigDecimal("0.05")),
    ELECTRONICS(new BigDecimal("0.35")),
    SPORTS_EQUIPMENT(new BigDecimal("0.30"));

    private BigDecimal risk;

    ModuleType(final BigDecimal risk) {
        this.risk = risk;
    }
}
