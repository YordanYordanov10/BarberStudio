package bg.softuni.barberstudio.Product.Service;

import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Repository.ProductRepository;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.BarberCreateProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createNewProduct(BarberCreateProduct barberCreateProduct, User barber) {

        Product product = Product.builder()
                .name(barberCreateProduct.getName())
                .description(barberCreateProduct.getDescription())
                .price(barberCreateProduct.getPrice())
                .addedByBarber(barber)
                .category(barberCreateProduct.getCategory())
                .build();

        productRepository.save(product);
    }
}
