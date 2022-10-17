package de.angelasensio.tariff.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class PolicyStore {

    private final Map<UUID, BigDecimal> policies = new ConcurrentHashMap<>();

    public BigDecimal store(final UUID policyId, BigDecimal price) {
        return policies.putIfAbsent(policyId, price);
    }

    public Optional<BigDecimal> get(final UUID policyId) {
        return Optional.ofNullable(policies.get(policyId));
    }

}
