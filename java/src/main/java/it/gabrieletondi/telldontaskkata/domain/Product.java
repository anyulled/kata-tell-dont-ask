package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.HALF_UP;

public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public Product(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getUnitaryTax() {
        return this.price
                .divide(BigDecimal.valueOf(100), HALF_UP)
                .multiply(this.category.getTaxPercentage())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
