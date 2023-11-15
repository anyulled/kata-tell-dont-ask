package it.gabrieletondi.telldontaskkata.usecase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderBuilder;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.doubles.TestShipmentService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderShipmentUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final TestShipmentService shipmentService = new TestShipmentService();
    private final OrderShipmentUseCase useCase = new OrderShipmentUseCase(orderRepository, shipmentService);

    @Test
    void shipApprovedOrder() {
        Order initialOrder = new OrderBuilder()
                .withId(1)
                .withStatus(OrderStatus.APPROVED)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder().getStatus()).isEqualTo(OrderStatus.SHIPPED);
        assertThat(shipmentService.getShippedOrder()).isEqualTo(initialOrder);
    }

    @Test
    void createdOrdersCannotBeShipped() {
        Order initialOrder = new OrderBuilder()
                .withId(1)
                .withStatus(OrderStatus.CREATED)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(OrderCannotBeShippedException.class);

        assertThat(orderRepository.getSavedOrder()).isNull();
        assertThat(shipmentService.getShippedOrder()).isNull();
    }

    @Test
    void rejectedOrdersCannotBeShipped() {
        Order initialOrder = new OrderBuilder()
                .withId(1)
                .withStatus(OrderStatus.REJECTED)
                .createOrder();

        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(OrderCannotBeShippedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
        assertThat(shipmentService.getShippedOrder()).isNull();
    }

    @Test
    void shippedOrdersCannotBeShippedAgain() {
        Order initialOrder = new OrderBuilder()
                .withId(1)
                .withStatus(OrderStatus.SHIPPED)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(OrderCannotBeShippedTwiceException.class);

        assertThat(orderRepository.getSavedOrder()).isNull();
        assertThat(shipmentService.getShippedOrder()).isNull();
    }
}
