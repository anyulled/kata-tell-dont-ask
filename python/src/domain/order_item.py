from dataclasses import dataclass, field

from .product import Product


@dataclass
class OrderItem:
    product: Product = field(default_factory=Product)
    quantity: int = 0
    taxed_amount: float = 0
    tax: float = 0

    def __init__(self, product, quantity):
        super().__init__()
        self.product = product
        self.quantity = quantity
        self.tax = self.calculate_tax_amount()
        self.taxed_amount = self.calculate_taxed_amount()

    def unitary_tax(self):
        return round(self.product.price / 100 * self.product.category.tax_percentage, 2)

    def unitary_taxed_amount(self):
        return round((self.product.price + self.unitary_tax()), 2)

    def calculate_taxed_amount(self):
        return round(self.unitary_taxed_amount() * self.quantity, 2)

    def calculate_tax_amount(self):
        return self.unitary_tax() * self.quantity
