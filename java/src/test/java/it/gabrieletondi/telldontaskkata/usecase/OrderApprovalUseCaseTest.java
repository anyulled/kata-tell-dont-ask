package it.gabrieletondi.telldontaskkata.usecase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderBuilder;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.usecase.exception.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.usecase.exception.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.usecase.exception.ShippedOrdersCannotBeChangedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderApprovalUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    void approvedExistingOrder() {
        Order initialOrder = new OrderBuilder()
                .withId(1)
                .withStatus(OrderStatus.CREATED)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.isApproved()).isTrue();
    }

    @Test
    void rejectedExistingOrder() {
        Order initialOrder = new OrderBuilder()
                .withStatus(OrderStatus.CREATED)
                .withId(1)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.isRejected()).isTrue();
    }

    @Test
    void cannotApproveRejectedOrder() {
        Order initialOrder = new OrderBuilder()
                .withId(1)
                .withStatus(OrderStatus.REJECTED)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(RejectedOrderCannotBeApprovedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    void cannotRejectApprovedOrder() {
        Order initialOrder = new OrderBuilder()
                .withStatus(OrderStatus.APPROVED)
                .withId(1)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ApprovedOrderCannotBeRejectedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    void shippedOrdersCannotBeApproved() {
        Order initialOrder = new OrderBuilder()
                .withStatus(OrderStatus.SHIPPED)
                .withId(1)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ShippedOrdersCannotBeChangedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    void shippedOrdersCannotBeRejected() {
        Order initialOrder = new OrderBuilder()
                .withStatus(OrderStatus.SHIPPED)
                .withId(1)
                .createOrder();
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ShippedOrdersCannotBeChangedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }
}
