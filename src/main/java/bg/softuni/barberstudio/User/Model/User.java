package bg.softuni.barberstudio.User.Model;


import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.Service.Model.BarberService;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String profilePicture;

    public boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "barberId", fetch = FetchType.EAGER)
    private List<BarberService> barberServices;

    @OneToMany(mappedBy = "barber", fetch = FetchType.EAGER)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(mappedBy = "addedByBarber", fetch = FetchType.EAGER)
    private List<Product> products;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.EAGER)
    private List<ProductOrder> purchasedProducts;

}
