package it.gabrieletondi.telldontaskkata.usecase;

import java.util.ArrayList;
import java.util.List;

public class SellItemsRequest {
    private final List<SellItemRequest> requests;

    public SellItemsRequest() {
        this.requests = new ArrayList<>();
    }

    public void addItemRequest(SellItemRequest sellItemRequest) {
        this.requests.add(sellItemRequest);
    }


    public List<SellItemRequest> getRequests() {
        return requests;
    }
}
