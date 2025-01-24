package bg.softuni.barberstudio.Appointment.Model;


import bg.softuni.barberstudio.Barber.Model.Barber;
import bg.softuni.barberstudio.Service.Model.Service;
import bg.softuni.barberstudio.User.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date date;

    private Time time;

    @ManyToOne
    private User user;

    @ManyToOne
    private Barber barber;

    @OneToOne
    private Service service;

}
