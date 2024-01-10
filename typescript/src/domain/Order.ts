import OrderItem from './OrderItem';
import {OrderStatus} from './OrderStatus';
import OrderApprovalRequest from '../useCase/OrderApprovalRequest';
import ShippedOrdersCannotBeChangedException from '../useCase/ShippedOrdersCannotBeChangedException';
import RejectedOrderCannotBeApprovedException from '../useCase/RejectedOrderCannotBeApprovedException';
import ApprovedOrderCannotBeRejectedException from '../useCase/ApprovedOrderCannotBeRejectedException';

class Order {
  constructor(status: OrderStatus, identifier: number, currency: string = 'EUR') {
    this.status = status;
    this.id = identifier;
    this.items = [];
    this.total = 0;
    this.tax = 0;
    this.currency = currency;
  }

  public isShipped(): boolean {
    return this.status === OrderStatus.SHIPPED;
  }

  public isRejected(): boolean {
    return this.status === OrderStatus.REJECTED;
  }

  public isApproved(): boolean {
    return this.status === OrderStatus.APPROVED;
  }

  public verifyApprovalStatus(request: OrderApprovalRequest): void {
    if (this.isShipped()) {
      throw new ShippedOrdersCannotBeChangedException();
    }

    if (request.isApproved() && this.isRejected()) {
      throw new RejectedOrderCannotBeApprovedException();
    }

    if (!request.isApproved() && this.isApproved()) {
      throw new ApprovedOrderCannotBeRejectedException();
    }
  }

  public addOrderItem(item: OrderItem): void {
    this.items.push(item);
    this.tax += item.getTax();
    this.total += item.getTaxedAmount();
  }

  private total: number;
  private currency: string;
  private items: OrderItem[];
  private tax: number;
  private status: OrderStatus;
  private id: number;

  public getTotal(): number {
    return this.total;
  }

  public setTotal(total: number): void {
    this.total = total;
  }

  public getCurrency(): string {
    return this.currency;
  }


  public getItems(): OrderItem[] {
    return this.items;
  }

  public getTax(): number {
    return this.tax;
  }

  public setTax(tax: number): void {
    this.tax = tax;
  }

  public getStatus(): OrderStatus {
    return this.status;
  }

  public setStatus(status: OrderStatus): void {
    this.status = status;
  }

  public getId(): number {
    return this.id;
  }
}

export default Order;

