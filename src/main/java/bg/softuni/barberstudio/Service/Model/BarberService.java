package bg.softuni.barberstudio.Service.Model;



import bg.softuni.barberstudio.User.Model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services")
public class BarberService {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private double price;

    @ManyToOne
    @JoinColumn(name = "barber_id",referencedColumnName = "id", nullable = false)
    private User barber;

}
