class OrderApprovalRequest {
  constructor(orderId: number, approved: boolean) {
    this.orderId = orderId;
    this.approved = approved;
  }

  private orderId: number;
  private approved: boolean;

  public getOrderId(): number {
    return this.orderId;
  }

  public isApproved(): boolean{
    return this.approved;
  }
}

export default OrderApprovalRequest;

