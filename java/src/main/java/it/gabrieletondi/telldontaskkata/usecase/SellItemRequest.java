package it.gabrieletondi.telldontaskkata.usecase;

public class SellItemRequest {
    private final int quantity;
    private final String productName;

    public SellItemRequest(String productName, int quantity) {
        this.quantity = quantity;
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }
}
