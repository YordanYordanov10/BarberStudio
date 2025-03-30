package bg.softuni.barberstudio.ProductOrder;

import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.ProductOrder.Repository.ProductOrderRepository;
import bg.softuni.barberstudio.ProductOrder.Service.ProductOrderService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.ProductOrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductOrderServiceUTest {

    @InjectMocks
    private ProductOrderService productOrderService;

    @Mock
    private ProductOrderRepository productOrderRepository;

    @Test
    void givenProductOrderAndBuyerAndBarber_thenCreateAndSaveToDatabase() {

        ProductOrderRequest productOrderRequest = new ProductOrderRequest();
        productOrderRequest.setQuantity(1);

        User buyer = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .build();

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("test123")
                .price(20)
                .build();

        ProductOrder mockProductOrder = ProductOrder.builder()
                .id(UUID.randomUUID())
                .buyer(buyer)
                .orderDate(LocalDateTime.now())
                .quantity(productOrderRequest.getQuantity())
                .product(product)
                .totalPrice(productOrderRequest.getQuantity() * product.getPrice())
                .build();

        when(productOrderRepository.save(any(ProductOrder.class))).thenReturn(mockProductOrder);

        ProductOrder newProductOrder = productOrderService.createOrderProduct(buyer,product,productOrderRequest);

        assertNotNull(newProductOrder);
        assertEquals(productOrderRequest.getQuantity(), newProductOrder.getQuantity());

    }
}
