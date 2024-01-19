from dataclasses import dataclass, field

from .order_item import OrderItem
from .order_status import OrderStatus
from ..use_case.exceptions import ShippedOrdersCannotBeChangedException, ApprovedOrderCannotBeRejectedException, \
    RejectedOrderCannotBeApprovedException


@dataclass
class Order:
    id: int = 0
    total: float = 0
    currency: str = 'EUR'
    items: list[OrderItem] = field(default_factory=list)
    tax: float = 0
    status: OrderStatus = OrderStatus.CREATED

    def is_shipped(self):
        return self.status == OrderStatus.SHIPPED

    def is_rejected(self):
        return self.status == OrderStatus.REJECTED

    def is_approved(self):
        return self.status == OrderStatus.APPROVED

    def approve(self, is_request_approved):
        if self.is_shipped():
            raise ShippedOrdersCannotBeChangedException()

        if is_request_approved and self.is_rejected():
            raise RejectedOrderCannotBeApprovedException()

        if not is_request_approved and self.is_approved():
            raise ApprovedOrderCannotBeRejectedException()

        self.status = OrderStatus.APPROVED if is_request_approved else OrderStatus.REJECTED
