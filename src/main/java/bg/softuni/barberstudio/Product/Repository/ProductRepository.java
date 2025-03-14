package bg.softuni.barberstudio.Product.Repository;

import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {


    List<Product> findAllByAddedByBarber(User barber);
}
