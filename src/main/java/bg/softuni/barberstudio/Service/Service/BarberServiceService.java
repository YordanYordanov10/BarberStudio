package bg.softuni.barberstudio.Service.Service;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Repository.BarberServiceRepository;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.BarberServiceCreate;
import bg.softuni.barberstudio.Web.Dto.BarberServiceEdit;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BarberServiceService {

    private final BarberServiceRepository barberServiceRepository;

    public BarberServiceService(BarberServiceRepository barberServiceRepository) {
        this.barberServiceRepository = barberServiceRepository;
    }


    public BarberService addNewService(BarberServiceCreate barberServiceCreate, User barber) {

        BarberService barberService = new BarberService();
        barberService.setName(barberServiceCreate.getName());
        barberService.setDescription(barberServiceCreate.getDescription());
        barberService.setPrice(barberServiceCreate.getPrice());
        barberService.setBarber(barber);

        return barberServiceRepository.save(barberService);
    }

    public List<BarberService> getAllServices(User barber) {
        return barberServiceRepository.findAllByBarber(barber);
    }

    public List<BarberService> getAllServicesByAddedByBarber(User barber) {
        List<BarberService> services = barberServiceRepository.findAllByBarber(barber);
        return services != null ? services : new ArrayList<>();
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

    public BarberService getBarberServiceById(UUID barberId) {
        return barberServiceRepository.findById(barberId).orElseThrow(()-> new DomainException("No such BarberService"));
    }

    public List<BarberService> getServicesByBarberId(UUID barberId) {
        List<BarberService> allByBarberId = barberServiceRepository.findAllByBarberId(barberId);
        return allByBarberId != null ? allByBarberId : new ArrayList<>();
    }
}
