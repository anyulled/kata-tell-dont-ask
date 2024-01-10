import Order from '../domain/Order';
import {OrderStatus} from '../domain/OrderStatus';
import OrderRepository from '../repository/OrderRepository';
import OrderApprovalRequest from './OrderApprovalRequest';

class OrderApprovalUseCase {
  private readonly orderRepository: OrderRepository;

  public constructor(orderRepository: OrderRepository){
    this.orderRepository = orderRepository;
  }

  public run(request: OrderApprovalRequest): void {
    const order: Order = this.orderRepository.getById(request.getOrderId());

    order.verifyApprovalStatus(request);

    order.setStatus(request.isApproved() ? OrderStatus.APPROVED : OrderStatus.REJECTED);
    this.orderRepository.save(order);
  }
}

export default OrderApprovalUseCase;
