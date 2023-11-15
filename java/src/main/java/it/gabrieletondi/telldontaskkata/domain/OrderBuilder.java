package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderBuilder {
    private int id;
    private OrderStatus status;
    private String currency;


    public OrderBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public Order createOrder() {
        return new Order(id, status, currency, new ArrayList<>(), BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public OrderBuilder withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

}