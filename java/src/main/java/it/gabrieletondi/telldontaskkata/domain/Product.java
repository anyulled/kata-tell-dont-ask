package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.HALF_UP;

public record Product(String name, BigDecimal price, Category category) {

    public BigDecimal getTaxAmount(int quantity) {
        return getUnitaryTax().multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getTaxedAmount(int quantity) {
        return this.getUnitaryTaxedAmount()
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, HALF_UP);
    }

    public BigDecimal getUnitaryTaxedAmount() {
        return price.add(getUnitaryTax()).setScale(2, HALF_UP);
    }

    public BigDecimal getUnitaryTax() {
        return this.price
                .divide(BigDecimal.valueOf(100), HALF_UP)
                .multiply(this.category.taxPercentage())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
