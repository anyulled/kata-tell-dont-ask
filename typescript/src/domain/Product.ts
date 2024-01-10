import Category from './Category';

class Product {
  private readonly name: string;
  private readonly price: number;

  constructor(name: string, price: number, category: Category) {
    this.name = name;
    this.category = category;
    this.price = price;
  }
  private category: Category;

  public getName(): string {
    return this.name;
  }

  public getPrice(): number {
    return this.price;
  }

  public calculateUnitaryTax(): number {
    return Math.round(this.price / 100 * this.category.getTaxPercentage() * 100) / 100;
  }

  public calculateUnitaryTaxedAmount(): number {
    return Math.round((this.price + this.calculateUnitaryTax()) * 100) / 100;
  }

  public calculateTaxedAmount(itemQuantity: number): number {
    return Math.round(this.calculateUnitaryTaxedAmount() * itemQuantity * 100) / 100;
  }

  public calculateTaxAmount(itemQuantity: number): number {
    return this.calculateUnitaryTax() * itemQuantity;
  }
}

export default Product;

