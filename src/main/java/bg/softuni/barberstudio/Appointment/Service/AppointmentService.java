package bg.softuni.barberstudio.Appointment.Service;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Repository.AppointmentRepository;
import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserService userService;

    public AppointmentService(AppointmentRepository appointmentRepository, UserService userService) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
    }

    private static final List<String> TIME_SLOTS = Arrays.asList(
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"
    );

    public Map<String, Boolean> getAvailableSlots(LocalDate date, UUID barberId) {
        User barber = userService.getById(barberId);

        System.out.println("Barber: " + barber.getId());
        System.out.println("Requested date: " + date);

        List<Appointment> appointments = appointmentRepository.findByAppointmentDateAndBarber(date, barber);

        System.out.println("Appointments found: " + appointments.size());
        appointments.forEach(a -> System.out.println("Appointment: " + a.getCustomerName()));

        List<String> bookedSlots = appointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toList());

        Map<String, Boolean> slots = new LinkedHashMap<>();
        for (String slot : TIME_SLOTS) {
            slots.put(slot, !bookedSlots.contains(slot));
        }

        return slots;
    }

    public void bookAppointment(String customerName, LocalDate date, String timeSlot,UUID userId, UUID barberId) {

        User user = userService.getById(userId);
        User barber = userService.getById(barberId);


        if (!getAvailableSlots(date, barberId).containsKey(timeSlot)) {
            throw new IllegalStateException("Time slot is not available");
        }

        
        Optional<Appointment> existingAppointment = appointmentRepository
                .findByCustomerNameAndAppointmentDateAndBarberAndTimeSlot(customerName, date, barber, timeSlot);

        if (existingAppointment.isPresent()) {
            throw new IllegalStateException("You already have an appointment on this date.");
        }

        Appointment appointment = new Appointment();
        appointment.setCustomerName(customerName);
        appointment.setAppointmentDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setUser(user);
        appointment.setBarber(barber);



        appointmentRepository.save(appointment);
    }


    public List<Appointment> getAppointmentsByBarber(UUID barberId) {

        User barber = userService.getById(barberId);

        return appointmentRepository.findByBarber(barber);
    }

}


