package de.angelasensio.tariff.service;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.angelasensio.tariff.domain.Module;
import de.angelasensio.tariff.domain.Policy;

@Service
public class CalculatorService {

    public BigDecimal calculatePrice(Policy policy) {
        BigDecimal total = BigDecimal.ZERO;

        Set<Module> modules = policy.getModules();
        if (modules.isEmpty()) {
            return total;
        }

        for (Module module : modules) {
            total = total.add(module.getType().getRisk().multiply(new BigDecimal(module.getCoverage())));
        }
        return total;
    }

}
