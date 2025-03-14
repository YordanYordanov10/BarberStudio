package bg.softuni.barberstudio.Service.Service;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Repository.BarberServiceRepository;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.BarberServiceCreate;
import bg.softuni.barberstudio.Web.Dto.BarberServiceEdit;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<BarberService> getAllServices(UUID id) {
        return barberServiceRepository.findAll();
    }

    public List<BarberService> getAllServicesByAddedByBarber(User barber) {
        return barberServiceRepository.findAllByBarberId(barber);
    }

    public void deleteBarberServiceById(UUID serviceId) {

        BarberService service = barberServiceRepository.findById(serviceId).orElseThrow(() -> new DomainException("No such BarberService found"));
        barberServiceRepository.delete(service);
    }

    public void editBarberServiceDetail(BarberServiceEdit barberServiceEdit, UUID serviceId) {

        BarberService service = barberServiceRepository.findById(serviceId).orElseThrow(() -> new DomainException("No such BarberService found"));

        service.setName(barberServiceEdit.getName());
        service.setDescription(barberServiceEdit.getDescription());
        service.setPrice(barberServiceEdit.getPrice());

        barberServiceRepository.save(service);
    }

}
