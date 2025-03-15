package bg.softuni.barberstudio.ProductOrder.Service;

import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.ProductOrder.Repository.ProductOrderRepository;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.ProductOrderRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }


    public void createOrderProduct(User buyer, Product product, ProductOrderRequest productOrderRequest) {

        ProductOrder productOrder = ProductOrder.builder()
                .buyer(buyer)
                .quantity(productOrderRequest.getQuantity())
                .orderDate(LocalDateTime.now())
                .product(product)
                .totalPrice(productOrderRequest.getQuantity() * product.getPrice())
                .build();

        productOrderRepository.save(productOrder);
    }

    public List<ProductOrder> getProductOrderByUserId(User buyer) {

         return productOrderRepository.getProductOrdersByBuyer(buyer);
    }
}
