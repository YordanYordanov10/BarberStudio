package bg.softuni.barberstudio.Barber.Model;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "barbers")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    private int experience;


    @OneToMany(mappedBy = "barber", fetch = FetchType.EAGER)
    private List<Appointment> appointments;






}
