package it.gabrieletondi.telldontaskkata.usecase;

import java.util.List;

public record SellItemsRequest(List<SellItemRequest> requests) {
}
