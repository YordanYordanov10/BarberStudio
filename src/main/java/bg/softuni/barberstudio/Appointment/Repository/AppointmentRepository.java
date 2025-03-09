package bg.softuni.barberstudio.Appointment.Repository;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByAppointmentDateAndBarber(LocalDate date, User barber);
    List<Appointment> findByBarber(User barber);

    Optional<Appointment> findByCustomerNameAndAppointmentDateAndBarberAndTimeSlot(String customerName, LocalDate date,User barber, String timeSlot);

    List<Appointment> findByUserId(UUID userId);

    void deleteAppointmentById(UUID appointmentId);
}
