package de.angelasensio.tariff.domain;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Result {
    private UUID policyId;
    private BigDecimal price;
}
