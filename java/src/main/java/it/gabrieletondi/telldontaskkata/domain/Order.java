package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.usecase.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.usecase.RejectedOrderCannotBeApprovedException;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.APPROVED;
import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;
import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.REJECTED;
import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.SHIPPED;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private final int id;

    protected Order(int id, OrderStatus status) {
        this.status = status;
        this.id = id;
    }

    public Order(int id, OrderStatus status, String currency, List<OrderItem> items, BigDecimal total, BigDecimal tax) {
        this.total = total;
        this.currency = currency;
        this.items = items;
        this.tax = tax;
        this.status = status;
        this.id = id;
    }

    public boolean isCreated() {
        return status.equals(CREATED);
    }

    public boolean isShipped() {
        return status.equals(SHIPPED);
    }

    public boolean isRejected() {
        return status.equals(REJECTED);
    }

    public boolean isApproved() {
        return status.equals(APPROVED);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void updateStatus(boolean requestIsApproved) {
        if (requestIsApproved && this.status.equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!requestIsApproved && this.status.equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        this.status = (requestIsApproved ? OrderStatus.APPROVED : OrderStatus.REJECTED);
    }

    public void addItem(OrderItem orderItem) {
        this.items.add(orderItem);
        this.total = this.total.add(orderItem.getTaxedAmount());
        this.tax = this.tax.add(orderItem.getTax());
    }

    public void ship() {
        this.status = OrderStatus.SHIPPED;
    }
}
