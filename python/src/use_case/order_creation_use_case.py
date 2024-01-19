from .exceptions import UnknownProductException
from .sell_items_request import SellItemsRequest
from ..domain.order import Order
from ..domain.order_item import OrderItem
from ..repository.order_repository import OrderRepository
from ..repository.product_catalog import ProductCatalog


class OrderCreationUseCase:
    def __init__(self, order_repository: OrderRepository, product_catalog: ProductCatalog):
        self._order_repository = order_repository
        self._product_catalog = product_catalog

    def run(self, request: SellItemsRequest) -> None:
        order = Order()

        for item_request in request.requests:
            product = self._product_catalog.get_by_name(item_request.product_name)

            if product is None:
                raise UnknownProductException()
            else:
                order_item = OrderItem(product=product, quantity=item_request.quantity)
                order.add_item(order_item)

        self._order_repository.save(order)
