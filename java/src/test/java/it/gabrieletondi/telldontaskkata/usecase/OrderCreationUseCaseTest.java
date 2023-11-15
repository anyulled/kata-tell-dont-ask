package it.gabrieletondi.telldontaskkata.usecase;

import it.gabrieletondi.telldontaskkata.domain.Category;
import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

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

        final SellItemsRequest request = new SellItemsRequest();
        request.addItemRequest(saladRequest);
        request.addItemRequest(tomatoRequest);

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(insertedOrder.isCreated()).isTrue();
            softAssertions.assertThat(insertedOrder.getTotal()).isEqualTo(new BigDecimal("23.37"));
            softAssertions.assertThat(insertedOrder.getTax()).isEqualTo(new BigDecimal("2.30"));
            softAssertions.assertThat(insertedOrder.getCurrency()).isEqualTo("EUR");
            softAssertions.assertThat(insertedOrder.getItems()).hasSize(2);
            softAssertions.assertThat(insertedOrder.getItems().get(0).getProduct().getName()).isEqualTo("salad");
            softAssertions.assertThat(insertedOrder.getItems().get(0).getProduct().getPrice()).isEqualTo(new BigDecimal("3.56"));
            softAssertions.assertThat(insertedOrder.getItems().get(0).getQuantity()).isEqualTo(2);
            softAssertions.assertThat(insertedOrder.getItems().get(0).getTaxedAmount()).isEqualTo(new BigDecimal("7.92"));
            softAssertions.assertThat(insertedOrder.getItems().get(0).getTax()).isEqualTo(new BigDecimal("0.80"));
            softAssertions.assertThat(insertedOrder.getItems().get(1).getProduct().getName()).isEqualTo("tomato");
            softAssertions.assertThat(insertedOrder.getItems().get(1).getProduct().getPrice()).isEqualTo(new BigDecimal("4.65"));
            softAssertions.assertThat(insertedOrder.getItems().get(1).getQuantity()).isEqualTo(3);
            softAssertions.assertThat(insertedOrder.getItems().get(1).getTaxedAmount()).isEqualTo(new BigDecimal("15.45"));
            softAssertions.assertThat(insertedOrder.getItems().get(1).getTax()).isEqualTo(new BigDecimal("1.50"));
        });
    }

    @Test
    void unknownProduct() {
        SellItemsRequest request = new SellItemsRequest();
        SellItemRequest unknownProductRequest = new SellItemRequest("unknown product", 1);
        request.addItemRequest(unknownProductRequest);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(UnknownProductException.class);
    }
}
