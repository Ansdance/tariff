package de.angelasensio.tariff.domain;

import static com.google.common.collect.Range.closed;

import com.google.common.collect.Range;
import lombok.Getter;

@Getter
public enum Coverage {

    BIKE(closed(0, 3000)),
    JEWELRY(closed(500, 10000)),
    ELECTRONICS(closed(500, 6000)),
    SPORTS_EQUIPMENT(closed(0, 20000));

    private Range coverageRange;

    Coverage(final Range<Integer> range) {
        coverageRange = range;
    }
}
