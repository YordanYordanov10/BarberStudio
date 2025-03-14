package bg.softuni.barberstudio.Service.Repository;


import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BarberServiceRepository extends JpaRepository<BarberService, UUID> {


    List<BarberService> findAllByBarberId(User barberId);
}
