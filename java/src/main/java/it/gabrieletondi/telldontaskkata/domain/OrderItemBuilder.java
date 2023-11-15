package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

public class OrderItemBuilder {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public OrderItemBuilder withProduct(Product product) {
        this.product = product;
        return this;
    }

    public OrderItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderItemBuilder withTaxedAmount(BigDecimal taxedAmount) {
        this.taxedAmount = taxedAmount;
        return this;
    }

    public OrderItemBuilder withTax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }

    public OrderItem createOrderItem() {
        return new OrderItem(product, quantity, taxedAmount, tax);
    }
}