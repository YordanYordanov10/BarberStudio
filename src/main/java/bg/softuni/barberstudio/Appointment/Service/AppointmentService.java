package bg.softuni.barberstudio.Appointment.Service;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Repository.AppointmentRepository;
import bg.softuni.barberstudio.Email.Service.NotificationService;
import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final BarberServiceService barberService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, UserService userService, NotificationService notificationService, BarberServiceService barberService) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.barberService = barberService;
    }

    private static final List<String> TIME_SLOTS = Arrays.asList(
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"
    );

    public Map<String, Boolean> getAvailableSlots(LocalDate date, UUID barberId) {
        User barber = userService.getById(barberId);

        List<Appointment> appointments = appointmentRepository.findByAppointmentDateAndBarber(date, barber);

        List<String> bookedSlots = appointments.stream()
                .map(Appointment::getTimeSlot)
                .toList();

        Map<String, Boolean> slots = new LinkedHashMap<>();
        for (String slot : TIME_SLOTS) {
            slots.put(slot, !bookedSlots.contains(slot));
        }

        return slots;
    }

    public void bookAppointment(String customerName, LocalDate date, String timeSlot,UUID userId, UUID barberId, UUID serviceId) {

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

        BarberService service = barberService.getBarberServiceById(serviceId);

        Appointment appointment = new Appointment();
        appointment.setCustomerName(customerName);
        appointment.setAppointmentDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setUser(user);
        appointment.setBarber(barber);
        appointment.setService(service);

        appointmentRepository.save(appointment);
        notificationService.sentNotification(userId,barberId,barber.getUsername(),barber.getEmail(),user.getEmail(),date, timeSlot,
                appointment.getService().getName(),appointment.getService().getPrice());
    }


    public List<Appointment> getAppointmentsByBarber(UUID barberId) {

        User barber = userService.getById(barberId);
        return appointmentRepository.findByBarber(barber);
    }

    public List<Appointment> getAppointmentsByUser(UUID userId) {

        return appointmentRepository.findByUserId(userId);
    }


    public void cancelAppointmentByUserId(UUID userId, UUID appointmentId) {

        List<Appointment> appointments = getAppointmentsByUser(userId);
        Appointment appointment = appointments.stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElseThrow(() -> new DomainException("No such appointment"));
        appointmentRepository.delete(appointment);
    }

    public Appointment getAppointmentById(UUID appointmentId) {

        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new DomainException("No such appointment"));
    }

    public void cancelAppointmentByBarberId(UUID barberId, UUID appointmentId) {
        List<Appointment> appointments = getAppointmentsByBarber(barberId);
        Appointment appointment = appointments.stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElseThrow(() -> new DomainException("No such appointment"));
        appointmentRepository.delete(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment findByBarberIdAndDateAndTime(UUID barberId, LocalDate appointmentDate, String timeSlot) {
        return appointmentRepository.findByAppointmentDateAndBarberIdAndTimeSlot(appointmentDate,barberId,timeSlot);
    }
}


