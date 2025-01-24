package bg.softuni.barberstudio.Appointment.Repository;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
