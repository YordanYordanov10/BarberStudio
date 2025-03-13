package bg.softuni.barberstudio.ProductOrder.Repository;

import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID> {
}
