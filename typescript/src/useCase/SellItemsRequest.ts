import SellItemRequest from './SellItemRequest';

class SellItemsRequest {


  constructor() {
    this.requests = [];
  }

  public addRequest(request: SellItemRequest): void {
    this.requests.push(request);
  }

  private requests: SellItemRequest[];

  public setRequests(requests: SellItemRequest[]): void {
    this.requests = requests;
  }

  public getRequests(): SellItemRequest[] {
    return this.requests;
  }
}

export default SellItemsRequest;
