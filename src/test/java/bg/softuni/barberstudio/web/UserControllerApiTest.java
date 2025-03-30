package bg.softuni.barberstudio.web;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Contact.Service.ContactService;
import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.ProductOrder.Service.ProductOrderService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
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

    @Autowired
    MockMvc mockMvc;

    private User mockUser1;
    private User mockUser2;
    private User mockAuthUser;
    private Comment comment;
    private Appointment appointment;
    private ProductOrder productOrder;
    private List<User> mockBarbers;
    private List<Comment> mockTestimonials;
    private List<Appointment> mockAppointments;
    private List<ProductOrder> mockProductOrders;
    private List<Appointment> mockBarberAppointments;
    private List<ProductOrder> mockBarberProductOrders;

    @BeforeEach
    void setUp() {
        UUID authenticatedUserId = UUID.randomUUID();
        mockUser1 = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        mockUser2 = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        comment = Comment.builder()
                .id(UUID.randomUUID())
                .barber(mockUser1)
                .author(mockUser2)
                .comment("lorem ipsum")
                .build();

        mockAuthUser = User.builder()
                .id(authenticatedUserId)
                .username("authenticatedUser")
                .email("auth@example.com")
                .password("password123")
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
                .totalPrice(10)
                .build();

        mockBarbers = List.of(mockUser1, mockUser2);
        mockTestimonials = List.of(comment);
        mockAppointments = List.of(appointment);
        mockProductOrders = List.of(productOrder);
        mockBarberAppointments = List.of(appointment);
        mockBarberProductOrders = List.of(productOrder);

    }

//    @Test
//    @WithMockUser
//    void getProfilePage_ShouldReturnProfilePageWithAttributes_ForAuthenticatedUser() throws Exception {
//        // Given
//        UUID authenticatedUserId = mockUser1.getId();
//
//        // Mock authentication
//        AuthenticationDetails authDetails = new AuthenticationDetails(
//                authenticatedUserId,
//                mockUser1.getUsername(),
//                mockUser1.getEmail(),
//                mockUser1.getRole(),
//                mockUser1.isActive()
//        );
//
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(new TestingAuthenticationToken(authDetails, null));
//        SecurityContextHolder.setContext(securityContext);
//
//        // Mock service responses
//        when(userService.getById(authenticatedUserId)).thenReturn(mockUser1);
//        when(userService.getById(authenticatedUserId)).thenReturn(mockUser2); // Differentiate barber
//        when(appointmentService.getAppointmentsByUser(authenticatedUserId)).thenReturn(mockAppointments);
//        when(productOrderService.getProductOrderByUserId(mockUser1)).thenReturn(mockProductOrders);
//        when(appointmentService.getAppointmentsByBarber(mockUser2.getId())).thenReturn(mockBarberAppointments);
//        when(productOrderService.getProductOrderByBarber(mockUser2)).thenReturn(mockBarberProductOrders);
//
//        // When & Then
//        mockMvc.perform(get("/profile"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("profile"))
//                .andExpect(model().attribute("user", mockUser1))
//                .andExpect(model().attributeExists("userEditRequest"))
//                .andExpect(model().attribute("appointments", mockAppointments))
//                .andExpect(model().attribute("orders", mockProductOrders))
//                .andExpect(model().attribute("barber", mockUser2)) // Explicitly include barber
//                .andExpect(model().attribute("barberAppointments", mockBarberAppointments))
//                .andExpect(model().attribute("barberOrders", mockBarberProductOrders));
//
//        // Verify service calls
//        verify(userService, times(1)).getById(authenticatedUserId);
//        verify(userService, times(1)).getById(authenticatedUserId);
//        verify(appointmentService, times(1)).getAppointmentsByUser(authenticatedUserId);
//        verify(productOrderService, times(1)).getProductOrderByUserId(mockUser1);
//        verify(appointmentService, times(1)).getAppointmentsByBarber(mockUser2.getId());
//        verify(productOrderService, times(1)).getProductOrderByBarber(mockUser2);
//    }

//    @GetMapping("/profile")
//    public ModelAndView getProfile(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
//
//        User user = userService.getById(authenticationDetails.getId());
//        User barber = userService.getById(authenticationDetails.getId());
//
//
//        List<Appointment> appointments = appointmentService.getAppointmentsByUser(authenticationDetails.getId());
//        List<ProductOrder> orders = productOrderService.getProductOrderByUserId(user);
//
//        List<Appointment> barberAppointments = appointmentService.getAppointmentsByBarber(barber.getId());
//        List<ProductOrder> barberOrders = productOrderService.getProductOrderByBarber(barber);
//
//        ModelAndView modelAndView = new ModelAndView("profile");
//        modelAndView.addObject("user", user);
//        modelAndView.addObject("userEditRequest", DtoMapper.mapUserToUserEditRequest(user));
//        modelAndView.addObject("appointments", appointments);
//        modelAndView.addObject("orders", orders);
//        modelAndView.addObject("barberAppointments", barberAppointments);
//        modelAndView.addObject("barberOrders", barberOrders);
//
//
//        return modelAndView;
//    }
}
