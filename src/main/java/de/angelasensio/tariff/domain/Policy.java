package de.angelasensio.tariff.domain;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Policy {

    private UUID policyId;
    private Set<Module> modules;

    public Policy() {
        // type 4 (pseudo randomly generated) UUID
        policyId = UUID.randomUUID();
        modules = Collections.emptySet();
    }

    public Policy(final Set<Module> modules) {
        this.policyId = UUID.randomUUID();
        this.modules = modules;
    }
}
