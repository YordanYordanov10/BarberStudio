package bg.softuni.barberstudio.web;

import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Model.ProductCategory;
import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.ProductOrder.Service.ProductOrderService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.BarberCreateProduct;
import bg.softuni.barberstudio.Web.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private  ProductOrderService productOrderService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private  ProductService productService;


    private User mockUser1;
    private User mockUser2;

    private Product validProduct;
    private Product mockProduct1;


    @BeforeEach
    void setUp() {

        mockUser1 = User.builder()
                .id(UUID.randomUUID())
                .username("barber_john")
                .email("john@barbershop.com")
                .password("secure123")
                .role(UserRole.BARBER)
                .build();

        mockUser2 = User.builder()
                .id(UUID.randomUUID())
                .username("client_mike")
                .email("mike@client.com")
                .password("client123")
                .role(UserRole.BARBER)
                .build();


        validProduct = Product.builder()
                .name("Premium Haircut")
                .price(35)
                .description("60 minute precision cut")
                .category(ProductCategory.HAIR_CARE)
                .build();

        mockProduct1 = Product.builder()
                .id(UUID.randomUUID())
                .name(validProduct.getName())
                .price(validProduct.getPrice())
                .addedByBarber(mockUser1)
                .build();


        SecurityContextHolder.clearContext();
    }

    @Test
    void addProductByBarberHappyPath() throws Exception {

        AuthenticationDetails authDetails = new AuthenticationDetails(
                mockUser1.getId(),
                mockUser1.getUsername(),
                mockUser1.getEmail(),
                UserRole.BARBER,
                mockUser1.isActive
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(authDetails, null, "ROLE_BARBER"));
        SecurityContextHolder.setContext(securityContext);


        when(userService.getById(mockUser1.getId())).thenReturn(mockUser1);
        when(productService.createNewProduct(any(),any())).thenReturn(mockProduct1);


        mockMvc.perform(post("/barber-panel/add-product")
                        .param("name", validProduct.getName())
                        .param("price", String.valueOf(validProduct.getPrice()))
                        .param("description", validProduct.getDescription())
                        .param("category", String.valueOf(validProduct.getCategory()))
                        .param("imageUrl", validProduct.getImageUrl())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/barber-panel"));

        // 4. Verify interactions
        verify(userService).getById(mockUser1.getId());
        verify(productService).createNewProduct(any(BarberCreateProduct.class), eq(mockUser1));
    }


//
//        User barber = userService.getById(authenticationDetails.getId());
//
//        if(bindingResult.hasErrors()){
//            return "redirect:/barber-panel";
//        }
//
//        productService.createNewProduct(barberCreateProduct, barber);
//
//        return "redirect:/barber-panel";
//    }
}
