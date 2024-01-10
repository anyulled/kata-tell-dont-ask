import Order from '../domain/Order';
import {OrderStatus} from '../domain/OrderStatus';
import OrderRepository from '../repository/OrderRepository';
import {ShipmentService} from '../service/ShipmentService';
import OrderShipmentRequest from './OrderShipmentRequest';

class OrderShipmentUseCase {
  private readonly orderRepository: OrderRepository;
  private readonly shipmentService: ShipmentService;

  public constructor (orderRepository: OrderRepository, shipmentService: ShipmentService) {
    this.orderRepository = orderRepository;
    this.shipmentService = shipmentService;
  }

  public run(request: OrderShipmentRequest): void {
    const order: Order = this.orderRepository.getById(request.getOrderId());

    order.verifyIsShippable();

    this.shipmentService.ship(order);

    order.setStatus(OrderStatus.SHIPPED);
    this.orderRepository.save(order);
  }
}

export default OrderShipmentUseCase;
