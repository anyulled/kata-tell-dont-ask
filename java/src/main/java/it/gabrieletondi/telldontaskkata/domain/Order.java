package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.usecase.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.usecase.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.usecase.OrderCannotBeShippedTwiceException;
import it.gabrieletondi.telldontaskkata.usecase.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.usecase.ShippedOrdersCannotBeChangedException;

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

    public void verifyOrderIsShippable() {
        if (isCreated() || isRejected()) {
            throw new OrderCannotBeShippedException();
        }

        if (isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }
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
        if (isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }
        if (requestIsApproved && isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!requestIsApproved && isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        this.status = (requestIsApproved ? OrderStatus.APPROVED : OrderStatus.REJECTED);
    }

    public void addItem(OrderItem orderItem) {
        this.items.add(orderItem);
        this.total = this.total.add(orderItem.taxedAmount());
        this.tax = this.tax.add(orderItem.tax());
    }

    public void ship() {
        verifyOrderIsShippable();
        this.status = OrderStatus.SHIPPED;
    }
}
