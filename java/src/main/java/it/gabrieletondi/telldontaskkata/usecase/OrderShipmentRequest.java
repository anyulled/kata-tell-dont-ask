package it.gabrieletondi.telldontaskkata.usecase;

public class OrderShipmentRequest {
    private final int orderId;

    public OrderShipmentRequest(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
