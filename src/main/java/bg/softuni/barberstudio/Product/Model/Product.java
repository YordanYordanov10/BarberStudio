package bg.softuni.barberstudio.Product.Model;


import bg.softuni.barberstudio.User.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private double price;

    private int quantity;

    private String productImage;

    private boolean available;

    @ManyToOne
    private User user;





}
