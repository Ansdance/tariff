package de.angelasensio.tariff.exception;

import static java.lang.String.format;

import java.util.UUID;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(final UUID uuid) {
        super(format("Could not find price for policyId: %s", uuid));
    }
}
