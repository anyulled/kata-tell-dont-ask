package it.gabrieletondi.telldontaskkata.usecase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderBuilder;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderItemBuilder;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.usecase.exception.UnknownProductException;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = new OrderBuilder()
                .withStatus(OrderStatus.CREATED)
                .withCurrency("EUR")
                .createOrder();

        for (SellItemRequest itemRequest : request.requests()) {
            Product product = productCatalog.getByName(itemRequest.productName());

            if (product == null) {
                throw new UnknownProductException();
            }

            final OrderItem orderItem = new OrderItemBuilder()
                    .withProduct(product)
                    .withQuantity(itemRequest.quantity())
                    .withTax(product.getTaxAmount(itemRequest.quantity()))
                    .withTaxedAmount(product.getTaxedAmount(itemRequest.quantity()))
                    .createOrderItem();
            order.addItem(orderItem);
        }

        orderRepository.save(order);
    }

}
