package bg.softuni.barberstudio;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Repository.AppointmentRepository;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.BarberServiceCreate;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookAppointmentITest {

    @Autowired
    private UserService userService;
    @Autowired
    private BarberServiceService barberService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();
    }

    @Test
    void shouldBookAppointmentSuccessfully() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("daka123")
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        RegisterRequest registerBarberRequest = RegisterRequest.builder()
                .username("barber123")
                .email("barber@gmail.com")
                .password("barber123")
                .build();

        BarberServiceCreate serviceCreate = BarberServiceCreate.builder()
                .name("Haircut")
                .description("Some text")
                .price(20)
                .build();

        User registerUser = userService.register(registerRequest);
        User registerBarber = userService.register(registerBarberRequest);
        registerUser.setRole(UserRole.USER);
        registerBarber.setRole(UserRole.BARBER);

        BarberService service = barberService.addNewService(serviceCreate, registerBarber);

        LocalDate date = LocalDate.now().plusDays(2);
        String timeSlot = "10:00";

        appointmentService.bookAppointment(registerUser.getUsername(), date, timeSlot, registerUser.getId(), registerBarber.getId(), service.getId());

        Optional<Appointment> optionalAppointment = appointmentRepository.findByCustomerNameAndAppointmentDateAndBarberAndTimeSlot(registerUser.getUsername(), date, registerBarber, timeSlot);

        assertTrue(optionalAppointment.isPresent());

        Appointment bookedAppointment = optionalAppointment.get();
        assertEquals(date, bookedAppointment.getAppointmentDate());
        assertEquals(registerBarber.getId(), bookedAppointment.getBarber().getId(), "Barber ID should match");
        assertEquals(timeSlot, bookedAppointment.getTimeSlot(), "Time slot should match");
        assertEquals(service.getId(), bookedAppointment.getService().getId(), "Service ID should match");
    }
}
