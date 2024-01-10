class SellItemRequest {
  constructor(name: string, quantity: number) {
    this.productName = name;
    this.quantity = quantity;
  }

  private quantity: number;
  private productName: string;

  public getQuantity(): number {
    return this.quantity;
  }

  public getProductName(): string {
    return this.productName;
  }
}

export default SellItemRequest;
