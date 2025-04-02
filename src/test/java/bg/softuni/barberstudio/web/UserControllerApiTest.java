package bg.softuni.barberstudio.web;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Contact.Model.Contact;
import bg.softuni.barberstudio.Contact.Service.ContactService;
import bg.softuni.barberstudio.Email.Service.NotificationService;
import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Model.ProductCategory;
import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.ProductOrder.Service.ProductOrderService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private  UserService userService;
    @MockitoBean
    private  AppointmentService appointmentService;
    @MockitoBean
    private  BarberServiceService barberServiceService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private  ProductService productService;
    @MockitoBean
    private  ProductOrderService productOrderService;
    @MockitoBean
    private ContactService contactService;
    @MockitoBean
    private NotificationService notificationService;


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
    void getProfilePage_ShouldReturnProfilePageWithAttributes_ForAuthenticatedUser() throws Exception {


        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null,"ROLE_USER"));
        SecurityContextHolder.setContext(securityContext);


        when(userService.getById(principal.getId())).thenReturn(mockUser);

        when(appointmentService.getAppointmentsByUser(principal.getId())).thenReturn(mockAppointments);
        when(productOrderService.getProductOrderByUserId(mockUser)).thenReturn(mockProductOrders);


        // When & Then
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attributeExists("userEditRequest"))
                .andExpect(model().attribute("appointments", mockAppointments))
                .andExpect(model().attribute("orders", mockProductOrders));

    }


    @Test
    void getBarberPanelWithRoleBarber() throws Exception {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null,"ROLE_BARBER"));
        SecurityContextHolder.setContext(securityContext);

        when(userService.getById(principal.getId())).thenReturn(mockBarber);

        when(barberServiceService.getAllServicesByAddedByBarber(mockBarber)).thenReturn(mockServices);
        when(productService.getAllProductsByAddedByBarber(mockBarber)).thenReturn(mockProducts);

        mockMvc.perform(get("/barber-panel"))
                .andExpect(status().isOk())
                .andExpect(view().name("barber-panel"))
                .andExpect(model().attribute("user", mockBarber))
                .andExpect(model().attributeExists("barberServiceCreate"))
                .andExpect(model().attributeExists("barberCreateProduct"))
                .andExpect(model().attributeExists("barberServiceEdit"))
                .andExpect(model().attributeExists("barberEditProduct"))
                .andExpect(model().attribute("services", mockServices))
                .andExpect(model().attribute("products", mockProducts));
    }

    @Test
    void getBarberPageWithId_shouldReturnBarberPageWithDetails() throws Exception {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null, "ROLE_ADMIN"));
        SecurityContextHolder.setContext(securityContext);


        when(userService.getById(principal.getId())).thenReturn(mockUser);
        when(userService.getById(mockBarber.getId())).thenReturn(mockBarber);
        when(barberServiceService.getAllServices(mockBarber)).thenReturn(mockServices);
        when(commentService.getAllCommentsForBarber(mockBarber.getId())).thenReturn(mockTestimonials);
        when(productService.getAllProducts(mockBarber)).thenReturn(mockProducts);


        mockMvc.perform(get("/barber/{id}", mockBarber.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("barber"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("barber", mockBarber))
                .andExpect(model().attribute("services", mockServices))
                .andExpect(model().attributeExists("commentCreateRequest"))
                .andExpect(model().attributeExists("productOrderRequest"))
                .andExpect(model().attribute("comments", mockTestimonials))
                .andExpect(model().attribute("products", mockProducts));

        verify(userService, times(1)).getById(principal.getId());
        verify(userService, times(1)).getById(mockBarber.getId());
        verify(barberServiceService, times(1)).getAllServices(mockBarber);
        verify(commentService, times(1)).getAllCommentsForBarber(mockBarber.getId());
        verify(productService, times(1)).getAllProducts(mockBarber);
    }


    @Test
    void getAdminPanelPageWithAuthentication_shouldReturnAdminPanelWithUsers() throws Exception {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null, "ROLE_ADMIN"));
        SecurityContextHolder.setContext(securityContext);

        when(userService.getById(principal.getId())).thenReturn(mockUser);
        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-panel"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("users", mockUsers))
                .andExpect(model().attribute("roles", UserRole.values()));

        verify(userService, times(1)).getById(principal.getId());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getDetailsInfoPageWithAuthenticatedAdmin_shouldReturnDetails() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null, "ROLE_ADMIN"));
        SecurityContextHolder.setContext(securityContext);

        productOrder.setProduct(product);
        appointment.setBarber(mockBarber);
        appointment.setService(service);

        when(userService.getById(principal.getId())).thenReturn(mockUser);
        when(appointmentService.getAllAppointments()).thenReturn(mockAppointments);
        when(productOrderService.getAllProductOrders()).thenReturn(mockProductOrders);
        when(contactService.getAllContact()).thenReturn(mockContacts);

        mockMvc.perform(get("/details-info"))
                .andExpect(status().isOk())
                .andExpect(view().name("details-info"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("appointments", mockAppointments))
                .andExpect(model().attribute("orders", mockProductOrders))
                .andExpect(model().attribute("messages", mockContacts));

        verify(userService, times(1)).getById(principal.getId());
        verify(appointmentService, times(1)).getAllAppointments();
        verify(productOrderService, times(1)).getAllProductOrders();
        verify(contactService, times(1)).getAllContact();

    }

}
