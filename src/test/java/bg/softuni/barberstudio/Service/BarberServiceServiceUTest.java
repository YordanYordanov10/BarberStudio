package bg.softuni.barberstudio.Service;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Repository.BarberServiceRepository;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.BarberServiceEdit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberServiceServiceUTest {


    @InjectMocks
    private BarberServiceService barberServiceService;

    @Mock
    private BarberServiceRepository barberServiceRepository;


    @Test
    void givenBarberServiceEditDto_shouldEditBarberService() {

        UUID serviceId = UUID.randomUUID();

        BarberService service = BarberService.builder()
                .id(serviceId)
                .name("Old Name")
                .description("Old Description")
                .price(10)
                .build();

        BarberServiceEdit barberServiceEdit = new BarberServiceEdit();
        barberServiceEdit.setName("New Service Name");
        barberServiceEdit.setDescription("Updated Description");
        barberServiceEdit.setPrice(15);

        when(barberServiceRepository.findById(serviceId)).thenReturn(Optional.of(service));


        barberServiceService.editBarberServiceDetail(barberServiceEdit, serviceId);


        assertEquals("New Service Name", service.getName());
        assertEquals("Updated Description", service.getDescription());
        assertEquals(15, service.getPrice());


        verify(barberServiceRepository, times(1)).save(service);
    }

    @Test
    void givenValidServiceId_shouldDeleteBarberService() {

        UUID serviceId = UUID.randomUUID();

        BarberService service = BarberService.builder()
                .id(serviceId)
                .build();

        when(barberServiceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        barberServiceService.deleteBarberServiceById(serviceId);

        verify(barberServiceRepository, times(1)).findById(serviceId);
        verify(barberServiceRepository, times(1)).delete(service);
    }

    @Test
    void givenInvalidServiceId_shouldThrowException() {

        UUID serviceId = UUID.randomUUID();

        when(barberServiceRepository.findById(serviceId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> barberServiceService.deleteBarberServiceById(serviceId));

        verify(barberServiceRepository, times(1)).findById(serviceId);
        verify(barberServiceRepository, never()).delete(any());
    }

    @Test
    void givenValidBarber_shouldReturnAllServices() {

        User barber = new User();
        List<BarberService> mockServices = List.of(
                new BarberService(UUID.randomUUID(), "Service 1", "Desc 1", 10, barber),
                new BarberService(UUID.randomUUID(), "Service 2", "Desc 2", 20, barber)
        );

        when(barberServiceRepository.findAllByBarber(barber)).thenReturn(mockServices);

        List<BarberService> result = barberServiceService.getAllServices(barber);

        assertEquals(mockServices.size(), result.size());
        assertEquals(mockServices, result);
        verify(barberServiceRepository, times(1)).findAllByBarber(barber);
    }

    @Test
    void givenBarberWithNoServices_shouldReturnEmptyList() {
        User barber = new User();

        when(barberServiceRepository.findAllByBarber(barber)).thenReturn(Collections.emptyList());

        List<BarberService> result = barberServiceService.getAllServices(barber);


        assertTrue(result.isEmpty());
        verify(barberServiceRepository, times(1)).findAllByBarber(barber);
    }

    @Test
    void givenValidBarber_shouldReturnAllServicesByAddedByBarber() {

        User barber = new User();
        List<BarberService> mockServices = List.of(
                new BarberService(UUID.randomUUID(), "Service 1", "Desc 1", 10, barber),
                new BarberService(UUID.randomUUID(), "Service 2", "Desc 2", 20, barber)
        );

        when(barberServiceRepository.findAllByBarber(barber)).thenReturn(mockServices);


        List<BarberService> result = barberServiceService.getAllServicesByAddedByBarber(barber);


        assertEquals(mockServices.size(), result.size());
        assertEquals(mockServices, result);
        verify(barberServiceRepository, times(1)).findAllByBarber(barber);
    }

    @Test
    void givenBarberWithNoServices_shouldReturnNewEmptyList() {

        User barber = new User();

        when(barberServiceRepository.findAllByBarber(barber)).thenReturn(null); // Симулираме null

        List<BarberService> result = barberServiceService.getAllServicesByAddedByBarber(barber);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(barberServiceRepository, times(1)).findAllByBarber(barber);
    }


}
