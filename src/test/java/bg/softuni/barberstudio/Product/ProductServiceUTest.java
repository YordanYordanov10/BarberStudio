package bg.softuni.barberstudio.Product;

import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Model.ProductCategory;
import bg.softuni.barberstudio.Product.Repository.ProductRepository;
import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.Web.Dto.BarberCreateProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void givenProductWithBarber_ThenCreateNewProduct() {

        BarberCreateProduct barberCreateProduct = BarberCreateProduct.builder()
                .name("test")
                .description("test test")
                .imageUrl("wwww.test.com")
                .price(15)
                .category(ProductCategory.HAIR_CARE)
                .build();

        User barber = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.BARBER)
                .build();

        Product mockProduct = Product.builder()
                .id(UUID.randomUUID())
                .name(barberCreateProduct.getName())
                .description(barberCreateProduct.getDescription())
                .imageUrl(barberCreateProduct.getImageUrl())
                .price(barberCreateProduct.getPrice())
                .category(barberCreateProduct.getCategory())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        Product createdProduct = productService.createNewProduct(barberCreateProduct,barber);

        assertNotNull(createdProduct);
        assertEquals(barberCreateProduct.getName(), createdProduct.getName());
        assertEquals(barberCreateProduct.getDescription(), createdProduct.getDescription());
        assertEquals(barberCreateProduct.getImageUrl(), createdProduct.getImageUrl());
        assertEquals(barberCreateProduct.getPrice(), createdProduct.getPrice());
        assertEquals(barberCreateProduct.getCategory(), createdProduct.getCategory());

    }

    @Test
    void givenProductByBarberInDatabase_whenFindByBarber_thenReturnThem(){

        User barber = User.builder()
                .id(UUID.randomUUID())
                .build();

        Product mockProductBarber = Product.builder()
                .id(UUID.randomUUID())
                .addedByBarber(barber)
                .build();

        when(productService.getAllProductsByAddedByBarber(barber)).thenReturn(List.of(mockProductBarber));

        List<Product> products = productService.getAllProductsByAddedByBarber(barber);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(barber.getId(), products.get(0).getAddedByBarber().getId());
    }

    @Test
    void givenProductDeleteHappyPath(){

        UUID productId = UUID.randomUUID();
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .build();

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        productService.deleteProductByProductId(productId);


        verify(productRepository, times(1)).delete(product);

    }

    @Test
    void givenProductNotFound_whenDelete_thenThrowDomainException() {

        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());


        DomainException thrownException = assertThrows(DomainException.class,
                () -> productService.deleteProductByProductId(productId));

        assertEquals("Product not found", thrownException.getMessage());
    }

}
