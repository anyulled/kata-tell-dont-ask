from dataclasses import dataclass, field

from .order_item import OrderItem
from .order_status import OrderStatus
from ..use_case.exceptions import ShippedOrdersCannotBeChangedException, ApprovedOrderCannotBeRejectedException, \
    RejectedOrderCannotBeApprovedException, OrderCannotBeShippedException, OrderCannotBeShippedTwiceException


@dataclass
class Order:
    __id: int = 0
    __total: float = 0
    __currency: str = 'EUR'
    __items: list[OrderItem] = field(default_factory=list)
    __tax: float = 0
    __status: OrderStatus = OrderStatus.CREATED

    def __init__(self, identifier=0, status=OrderStatus.CREATED):
        super().__init__()
        self.__id = identifier
        self.__status = status
        self.__items = []
        self.__currency = "EUR"
        self.__total = 0
        self.__tax = 0

    def is_created(self):
        return self.status == OrderStatus.CREATED

    def is_shipped(self):
        return self.status == OrderStatus.SHIPPED

    def is_rejected(self):
        return self.status == OrderStatus.REJECTED

    def is_approved(self):
        return self.status == OrderStatus.APPROVED

    @property
    def id(self):
        return self.__id

    @property
    def order_items(self):
        return self.__items

    @property
    def status(self):
        return self.__status

    @property
    def total(self):
        return self.__total

    @property
    def tax(self):
        return self.__tax

    @property
    def currency(self):
        return self.__currency

    @property
    def items(self):
        return self.__items

    def approve(self, is_request_approved):
        if self.is_shipped():
            raise ShippedOrdersCannotBeChangedException()

        if is_request_approved and self.is_rejected():
            raise RejectedOrderCannotBeApprovedException()

        if not is_request_approved and self.is_approved():
            raise ApprovedOrderCannotBeRejectedException()

        self.__status = OrderStatus.APPROVED if is_request_approved else OrderStatus.REJECTED

    def ship(self):
        if self.is_created() or self.is_rejected():
            raise OrderCannotBeShippedException()

        if self.is_shipped():
            raise OrderCannotBeShippedTwiceException()

        self.__status = OrderStatus.SHIPPED

    def add_item(self, item):
        self.__items.append(item)
        self.__total += item.taxed_amount
        self.__tax += item.tax
