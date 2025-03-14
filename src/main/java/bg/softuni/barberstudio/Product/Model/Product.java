package bg.softuni.barberstudio.Product.Model;

import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.User.Model.User;
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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @ManyToOne()
    @JoinColumn(name = "added_by_barber_id")
    private User addedByBarber;

    @OneToMany(mappedBy = "product")
    private List<ProductOrder> purchases;
}
