package bg.softuni.barberstudio.Appointment;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Repository.AppointmentRepository;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AppointmentUTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserService userService;

    @Test
    void givenBarberIdShouldReturnAppointment(){

        UUID barberId = UUID.randomUUID();

        User barber = User.builder()
                .id(UUID.randomUUID())
                .build();

        Appointment mockAppointment = Appointment.builder()
                .barber(barber)
                .build();

        when(userService.getById(barberId)).thenReturn(barber);
        when(appointmentRepository.findByBarber(any())).thenReturn(List.of(mockAppointment));

        List<Appointment> appointmentsByBarber = appointmentService.getAppointmentsByBarber(barberId);

        assertNotNull(appointmentsByBarber);
        assertEquals(appointmentsByBarber.size(),1);
    }

    @Test
    void givenUserIdShouldReturnAppointment(){

        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        Appointment mockAppointment = Appointment.builder()
                .user(user)
                .build();

        when(appointmentRepository.findByUserId(any())).thenReturn(List.of(mockAppointment));

        List<Appointment> appointmentsByUser = appointmentService.getAppointmentsByUser(userId);

        assertNotNull(appointmentsByUser);
        assertEquals(appointmentsByUser.size(),1);
    }

    @Test
    void givenUserIdAndAppointmentIdShouldFindAppointmentAndCancel(){

        UUID userId = UUID.randomUUID();
        UUID appointmentId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .build();

        Appointment mockAppointment = Appointment.builder()
                .id(appointmentId)
                .user(user)
                .build();


        when(appointmentService.getAppointmentsByUser(userId)).thenReturn(List.of(mockAppointment));

        doNothing().when(appointmentRepository).delete(mockAppointment);

        appointmentService.cancelAppointmentByUserId(userId, appointmentId);

        verify(appointmentRepository, times(1)).delete(mockAppointment);
    }

    @Test
    void givenBarberIdAndAppointmentIdShouldFindAppointmentAndCancel(){

        UUID barberId = UUID.randomUUID();
        UUID appointmentId = UUID.randomUUID();

        User barber = User.builder()
                .id(barberId)
                .build();

        Appointment mockAppointment = Appointment.builder()
                .id(appointmentId)
                .barber(barber)
                .build();


        when(appointmentService.getAppointmentsByBarber(barberId)).thenReturn(List.of(mockAppointment));

        doNothing().when(appointmentRepository).delete(mockAppointment);

        appointmentService.cancelAppointmentByBarberId(barberId, appointmentId);

        verify(appointmentRepository, times(1)).delete(mockAppointment);
    }


//       public void cancelAppointmentByBarberId(UUID barberId, UUID appointmentId) {
//        List<Appointment> appointments = getAppointmentsByBarber(barberId);
//        Appointment appointment = appointments.stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElseThrow(() -> new DomainException("No such appointment"));
//        appointmentRepository.delete(appointment);
//    }


}
