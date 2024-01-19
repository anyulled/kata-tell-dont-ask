from .order_approval_request import OrderApprovalRequest
from ..repository.order_repository import OrderRepository


class OrderApprovalUseCase:
    def __init__(self, order_repository: OrderRepository) -> None:
        self._order_repository = order_repository

    def run(self, request: OrderApprovalRequest) -> None:
        order = self._order_repository.get_by_id(request.order_id)

        order.approve(request.approved)

        self._order_repository.save(order)
