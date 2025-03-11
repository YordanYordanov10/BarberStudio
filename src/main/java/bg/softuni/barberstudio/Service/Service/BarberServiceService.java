package bg.softuni.barberstudio.Service.Service;

import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Repository.BarberServiceRepository;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.BarberServiceCreate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BarberServiceService {

    private final BarberServiceRepository barberServiceRepository;

    public BarberServiceService(BarberServiceRepository barberServiceRepository) {
        this.barberServiceRepository = barberServiceRepository;
    }


    public void addNewService(BarberServiceCreate barberServiceCreate, User barber) {

        BarberService barberService = new BarberService();
        barberService.setName(barberServiceCreate.getName());
        barberService.setDescription(barberServiceCreate.getDescription());
        barberService.setPrice(barberServiceCreate.getPrice());
        barberService.setBarberId(barber);

        


        barberServiceRepository.save(barberService);
    }
}
