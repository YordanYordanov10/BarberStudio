package bg.softuni.barberstudio.Barber.Model;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.User.Model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "barbers")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "clients_count")
    private int clientsCount;

    @Column(name = "user_rating")
    private BigDecimal userRating;

    @OneToMany(mappedBy = "barber")
    private List<Appointment> appointments;






}
