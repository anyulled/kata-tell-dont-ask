import SellItemRequest from './SellItemRequest';

class SellItemsRequest {

  private requests: SellItemRequest[];

  constructor() {
    this.requests = [];
  }

  public addRequest(request: SellItemRequest): void {
    this.requests.push(request);
  }

  public setRequests(requests: SellItemRequest[]): void {
    this.requests = requests;
  }

  public getRequests(): SellItemRequest[] {
    return this.requests;
  }
}

export default SellItemsRequest;
