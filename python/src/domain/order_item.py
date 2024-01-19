from dataclasses import dataclass, field

from .product import Product


@dataclass
class OrderItem:
    __product: Product = field(default_factory=Product)
    __quantity: int = 0
    __taxed_amount: float = 0
    __tax: float = 0

    def __init__(self, product, quantity):
        super().__init__()
        self.__product = product
        self.__quantity = quantity
        self.__tax = self.calculate_tax_amount()
        self.__taxed_amount = self.calculate_taxed_amount()

    @property
    def quantity(self):
        return self.__quantity

    @property
    def taxed_amount(self):
        return self.__taxed_amount

    @property
    def tax(self):
        return self.__tax

    @property
    def product(self):
        return self.__product

    def unitary_tax(self):
        return round(self.product.price / 100 * self.product.category.tax_percentage, 2)

    def unitary_taxed_amount(self):
        return round((self.product.price + self.unitary_tax()), 2)

    def calculate_taxed_amount(self):
        return round(self.unitary_taxed_amount() * self.quantity, 2)

    def calculate_tax_amount(self):
        return self.unitary_tax() * self.quantity
