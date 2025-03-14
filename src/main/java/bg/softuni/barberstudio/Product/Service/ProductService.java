package bg.softuni.barberstudio.Product.Service;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Repository.ProductRepository;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.BarberCreateProduct;
import bg.softuni.barberstudio.Web.Dto.BarberEditProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
                .imageUrl(barberCreateProduct.getImageUrl())
                .build();

        productRepository.save(product);
    }

    public List<Product> getAllProducts(User barber) {

        return productRepository.findAllByAddedByBarber(barber);
    }

    public List<Product> getAllProductsByAddedByBarber(User user) {

        return productRepository.findAllByAddedByBarber(user);
    }



    public void deleteProductByProductId(UUID productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new DomainException("Product not found"));

        productRepository.delete(product);
    }

    public void editBarberProductDetail(BarberEditProduct barberEditProduct, UUID productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new DomainException("Product not found"));

        product.setName(barberEditProduct.getName());
        product.setDescription(barberEditProduct.getDescription());
        product.setPrice(barberEditProduct.getPrice());

        productRepository.save(product);
    }
}
