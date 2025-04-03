package bg.softuni.barberstudio.web;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Contact.Service.ContactService;
import bg.softuni.barberstudio.Email.Service.NotificationService;
import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.ProductOrder.Service.ProductOrderService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class AppointmentControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AppointmentService appointmentService;
    @MockitoBean
    private BarberServiceService barberServiceService;
    @MockitoBean
    private NotificationService notificationService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private ContactService contactService;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private ProductOrderService productOrderService;

    @Autowired
    MockMvc mockMvc;

    private AuthenticationDetails principal;
    private User mockBarber;
    private User mockUser;
    private BarberService service;
    private Appointment appointment;

    private List<BarberService> mockServices;
    private List<User> mockBarbers;
    private List<Appointment> mockAppointments;

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

        service = BarberService.builder()
                .id(UUID.randomUUID())
                .barber(mockBarber)
                .price(20)
                .description("lorem ipsum")
                .name("test name")
                .build();

        appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .appointmentDate(LocalDate.of(2025,04,01))
                .timeSlot("10:00")
                .customerName("daka123")
                .build();


        mockBarbers = List.of(mockBarber);
        mockServices = List.of(service);

    }

    @Test
    void givenBarberIdAndDate_shouldMakeBookingHappyPath() throws Exception {


        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null,"ROLE_USER"));
        SecurityContextHolder.setContext(securityContext);

        UUID barberId = mockBarber.getId();
        LocalDate testDate = LocalDate.now().plusDays(2);
        appointment.setService(service);


        when(userService.getById(principal.getId())).thenReturn(mockUser);
        when(userService.findByUserRole()).thenReturn(mockBarbers);
        when(barberServiceService.getServicesByBarberId(barberId)).thenReturn(mockServices);
        when(appointmentService.getAvailableSlots(testDate, barberId))
                .thenReturn(Map.of("10:00", true, "11:00", true));
        mockMvc.perform(get("/booking")
                        .param("barberId", barberId.toString())
                        .param("date", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("booking"))
                .andExpect(model().attribute("barbers", mockBarbers))
                .andExpect(model().attribute("services", mockServices))
                .andExpect(model().attributeExists("slots"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("date", testDate))
                .andExpect(model().attribute("barberId", barberId));
    }

    @Test
    void givenParams_shouldReturnCreatedBookingAppointment() throws Exception {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null,"ROLE_USER"));
        SecurityContextHolder.setContext(securityContext);

        UUID userId = principal.getId();
        UUID barberId = mockBarber.getId();
        LocalDate testDate = LocalDate.now().plusDays(2);
        String customerName = principal.getUsername();
        String timeSlot = "10:00";

        appointmentService.bookAppointment(customerName,testDate,timeSlot,userId,barberId,service.getId());

        MockHttpServletRequestBuilder request = post("/booking")
                .param("timeSlot", "14:30")
                .param("barberId", "550e8400-e29b-41d4-a716-446655440000")
                .param("date", "2025-04-10")
                .param("serviceId", "f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/booking"));

                verify(appointmentService,times(1)).bookAppointment(customerName,testDate,timeSlot,userId,barberId,service.getId());
    }

    @Test
    void givenAppointmentId_ShouldDeleteAppointment() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null,"ROLE_USER"));
        SecurityContextHolder.setContext(securityContext);

        when(appointmentService.getAppointmentById(appointment.getId())).thenReturn(appointment);

//

        mockMvc.perform(delete("/booking/cancel/{appointmentId}",appointment.getId())
                        .with(csrf()))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/profile"));


        verify(appointmentService,times(1)).getAppointmentById(appointment.getId());

        verify(appointmentService,times(1)).cancelAppointmentByUserId(principal.getId(),appointment.getId());
        verify(notificationService,times(1)).deleteEmailNotification(principal.getId(),appointment.getAppointmentDate(),appointment.getTimeSlot());
}

//

}
