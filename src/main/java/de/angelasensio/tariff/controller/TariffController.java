package de.angelasensio.tariff.controller;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import de.angelasensio.tariff.domain.Coverage;
import de.angelasensio.tariff.domain.Module;
import de.angelasensio.tariff.domain.Policy;
import de.angelasensio.tariff.domain.Result;
import de.angelasensio.tariff.service.CalculatorService;
import de.angelasensio.tariff.service.PolicyStore;
import de.angelasensio.tariff.exception.CoverageOutOfRangeException;
import de.angelasensio.tariff.exception.PriceNotFoundException;

@RestController
@RequestMapping("/tariff")
@RequiredArgsConstructor
@Slf4j
public class TariffController {

    private final CalculatorService calculatorService;
    private final PolicyStore policyStore;

    @PostMapping
    public ResponseEntity<Result> calculatePolicyPrice(@RequestBody final Policy policy) {
        validatePolicy(policy);
        LOG.info("{}", policy);

        BigDecimal price = calculatorService.calculatePrice(policy);

        BigDecimal store = policyStore.store(policy.getPolicyId(), price);

        Result result = Result.builder()
                .policyId(policy.getPolicyId())
                .price(price)
                .build();
        LOG.info("final result: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{policyId}")
    public ResponseEntity<BigDecimal> retrievePriceForPolicyId(@PathVariable final UUID policyId) {
        requireNonNull(policyId, "policyId cannot be null");
        LOG.info("{}", policyId);
        Optional<BigDecimal> price = policyStore.get(policyId);
        BigDecimal policyPrice = price.orElseThrow(() -> new PriceNotFoundException(policyId));
        LOG.info("price: {}", policyPrice);
        return new ResponseEntity<>(policyPrice, HttpStatus.OK);
    }

    private void validatePolicy(final Policy policy) {
        requireNonNull(policy, "policy cannot be null");
        Set<Module> modules = policy.getModules();
        requireNonNull(modules, "modules cannot be null");

        if (!modules.isEmpty()) {
            for (Module m : modules) {
                switch (m.getType()) {
                    case BIKE:
                        validateCoverageValue(m, Coverage.BIKE);
                        break;
                    case ELECTRONICS:
                        validateCoverageValue(m, Coverage.ELECTRONICS);
                        break;
                    case JEWELRY:
                        validateCoverageValue(m, Coverage.JEWELRY);
                        break;
                    case SPORTS_EQUIPMENT:
                        validateCoverageValue(m, Coverage.SPORTS_EQUIPMENT);
                        break;
                }
            }
        }
    }

    private void validateCoverageValue(final Module m, final Coverage coverage) {
        if (!coverage.getCoverageRange().contains(m.getCoverage())) {
            throw new CoverageOutOfRangeException(format("coverage: %d for type %s is not contained in the range: %s",
                    m.getCoverage(),
                    m.getType(),
                    coverage.getCoverageRange().toString()));
        }
    }

}
