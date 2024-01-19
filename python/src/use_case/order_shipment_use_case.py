from .order_shipment_request import OrderShipmentRequest
from ..repository.order_repository import OrderRepository
from ..service.shipment_service import ShipmentService


class OrderShipmentUseCase:
    def __init__(self, order_repository: OrderRepository, shipment_service: ShipmentService):
        self._order_repository = order_repository
        self._shipment_service = shipment_service

    def run(self, request: OrderShipmentRequest) -> None:
        order = self._order_repository.get_by_id(request.order_id)

        order.ship()

        self._shipment_service.ship(order)
        self._order_repository.save(order)
