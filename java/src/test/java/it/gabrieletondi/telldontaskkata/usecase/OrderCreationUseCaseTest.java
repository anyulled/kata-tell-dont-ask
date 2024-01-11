package it.gabrieletondi.telldontaskkata.usecase;

import it.gabrieletondi.telldontaskkata.domain.Category;
import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.usecase.exception.UnknownProductException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final Category food = new Category("food", new BigDecimal("10"));
    private final ProductCatalog productCatalog = new InMemoryProductCatalog(
            Arrays.asList(
                    new Product("salad", new BigDecimal("3.56"), food)
                    ,
                    new Product("tomato", new BigDecimal("4.65"), food)

            )
    );
    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    void sellMultipleItems() {
        SellItemRequest saladRequest = new SellItemRequest("salad", 2);
        SellItemRequest tomatoRequest = new SellItemRequest("tomato", 3);

        final SellItemsRequest request = new SellItemsRequest(List.of(saladRequest, tomatoRequest));

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(insertedOrder.isCreated()).isTrue();
            softAssertions.assertThat(insertedOrder.getTotal()).isEqualTo(new BigDecimal("23.37"));
            softAssertions.assertThat(insertedOrder.getTax()).isEqualTo(new BigDecimal("2.30"));
            softAssertions.assertThat(insertedOrder.getCurrency()).isEqualTo("EUR");
            softAssertions.assertThat(insertedOrder.getItems()).hasSize(2);
            softAssertions.assertThat(insertedOrder.getItems().get(0).product().name()).isEqualTo("salad");
            softAssertions.assertThat(insertedOrder.getItems().get(0).product().price()).isEqualTo(new BigDecimal("3.56"));
            softAssertions.assertThat(insertedOrder.getItems().get(0).quantity()).isEqualTo(2);
            softAssertions.assertThat(insertedOrder.getItems().get(0).taxedAmount()).isEqualTo(new BigDecimal("7.92"));
            softAssertions.assertThat(insertedOrder.getItems().get(0).tax()).isEqualTo(new BigDecimal("0.80"));
            softAssertions.assertThat(insertedOrder.getItems().get(1).product().name()).isEqualTo("tomato");
            softAssertions.assertThat(insertedOrder.getItems().get(1).product().price()).isEqualTo(new BigDecimal("4.65"));
            softAssertions.assertThat(insertedOrder.getItems().get(1).quantity()).isEqualTo(3);
            softAssertions.assertThat(insertedOrder.getItems().get(1).taxedAmount()).isEqualTo(new BigDecimal("15.45"));
            softAssertions.assertThat(insertedOrder.getItems().get(1).tax()).isEqualTo(new BigDecimal("1.50"));
        });
    }

    @Test
    void unknownProduct() {
        SellItemRequest unknownProductRequest = new SellItemRequest("unknown product", 1);
        SellItemsRequest request = new SellItemsRequest(List.of(unknownProductRequest));

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(UnknownProductException.class);
    }
}
