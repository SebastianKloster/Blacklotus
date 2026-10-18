package com.kartago.blacklotus.printing.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Embeddable
public class Prices {
    @Column(precision = 10, scale = 2)
    private BigDecimal priceUsd;
    @Column(precision = 10, scale = 2)
    private BigDecimal priceUsdFoil;
    @Column(precision = 10, scale = 2)
    private BigDecimal priceUsdEtched;
}
