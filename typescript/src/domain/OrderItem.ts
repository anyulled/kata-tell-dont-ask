import Product from './Product';

class OrderItem {
  constructor(product: Product, quantity: number, taxAmount: number, taxedAmount: number) {
    this.product = product;
    this.quantity = quantity;
    this.tax = taxAmount;
    this.taxedAmount = taxedAmount;
  }

  private product: Product;
  private quantity: number;
  private taxedAmount: number;
  private tax: number;

  public getProduct(): Product {
    return this.product;
  }

  public getQuantity(): number {
    return this.quantity;
  }

  public getTaxedAmount(): number {
    return this.taxedAmount;
  }

  public getTax(): number {
    return this.tax;
  }

}

export default OrderItem;

