package bg.softuni.barberstudio.User.Model;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Service.Model.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    private String profilePicture;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Service> services;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Product> products;
}
