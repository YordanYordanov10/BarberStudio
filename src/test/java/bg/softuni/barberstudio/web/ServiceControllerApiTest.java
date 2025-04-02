package bg.softuni.barberstudio.web;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Contact.Model.Contact;
import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Model.ProductCategory;
import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.ServiceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceController.class)
public class ServiceControllerApiTest {

    @MockitoBean
    private  UserService userService;
    @MockitoBean
    private  BarberServiceService barberServiceService;

    @Autowired
    MockMvc mockMvc;

    private AuthenticationDetails principal;
    private User mockBarber;
    private User mockUser;
    private Comment comment;
    private Appointment appointment;
    private ProductOrder productOrder;
    private BarberService service;
    private Product product;
    private Contact contact;

    private List<User> mockBarbers;
    private List<Comment> mockTestimonials;
    private List<Appointment> mockAppointments;
    private List<ProductOrder> mockProductOrders;
    private List<Appointment> mockBarberAppointments;
    private List<ProductOrder> mockBarberProductOrders;
    private List<BarberService> mockServices;
    private List<Product> mockProducts;
    private List<User> mockUsers;
    private List<Contact> mockContacts;

    @BeforeEach
    void setUp() {

        principal = AuthenticationDetails.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .password("daka123")
                .isActive(true)
                .role(UserRole.ADMIN)
                .build();

        mockBarber = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .role(UserRole.BARBER)
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .role(UserRole.BARBER)
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        comment = Comment.builder()
                .id(UUID.randomUUID())
                .barber(mockBarber)
                .author(mockUser)
                .comment("lorem ipsum")
                .build();

        appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .appointmentDate(LocalDate.of(2025,04,01))
                .timeSlot("10:00")
                .customerName("daka123")
                .build();

        productOrder = ProductOrder.builder()
                .id(UUID.randomUUID())
                .quantity(2)
                .product(product)
                .buyer(mockUser)
                .totalPrice(10)
                .build();

        service = BarberService.builder()
                .id(UUID.randomUUID())
                .barber(mockBarber)
                .price(20)
                .description("lorem ipsum")
                .name("test name")
                .build();

        product = Product.builder()
                .name("test name")
                .price(15)
                .addedByBarber(mockBarber)
                .category(ProductCategory.HAIR_CARE)
                .imageUrl("www.image.com")
                .description("some description")
                .id(UUID.randomUUID())
                .build();

        contact = Contact.builder()
                .id(UUID.randomUUID())
                .message("some message")
                .email("some@gmail.com")
                .name("test name")
                .build();


        mockBarbers = List.of(mockBarber);
        mockTestimonials = List.of(comment);
        mockAppointments = List.of(appointment);
        mockProductOrders = List.of(productOrder);
        mockBarberAppointments = List.of(appointment);
        mockBarberProductOrders = List.of(productOrder);
        mockServices = List.of(service);
        mockProducts = List.of(product);
        mockUsers = List.of(mockUser);
        mockContacts = List.of(contact);

    }

    @Test
    void getServicesPageWithAuthentication() throws Exception {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null,"ROLE_USER"));
        SecurityContextHolder.setContext(securityContext);


        service.setBarber(mockBarber);
        UUID testBarberId = mockBarber.getId();


        when(userService.getById(principal.getId())).thenReturn(mockUser);
        when(userService.findByUserRole()).thenReturn(mockBarbers);
        when(barberServiceService.getServicesByBarberId(testBarberId)).thenReturn(mockServices);


        mockMvc.perform(get("/services")
                        .param("barberId", testBarberId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("services"))
                .andExpect(model().attribute("barbers", mockBarbers))
                .andExpect(model().attribute("services", mockServices))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("selectedBarberId", testBarberId));

        verify(userService, times(1)).getById(principal.getId());
        verify(userService, times(1)).findByUserRole();
        verify(barberServiceService, times(1)).getServicesByBarberId(testBarberId);
    }

}
