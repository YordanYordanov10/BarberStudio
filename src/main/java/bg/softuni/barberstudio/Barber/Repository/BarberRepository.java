package bg.softuni.barberstudio.Barber.Repository;

import bg.softuni.barberstudio.Barber.Model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface BarberRepository extends JpaRepository<Barber, UUID> {
}
