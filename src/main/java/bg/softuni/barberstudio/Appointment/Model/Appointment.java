package bg.softuni.barberstudio.Appointment.Model;


import bg.softuni.barberstudio.Barber.Model.Barber;
import bg.softuni.barberstudio.Service.Model.Service;
import bg.softuni.barberstudio.User.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "barber_id", referencedColumnName = "id")
    private Barber barber;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    private LocalDateTime appointmentDate;

}
