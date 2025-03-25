package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.Email.Service.NotificationService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;

@Controller
public class AppointmentController {

    private final UserService userService;
    private final AppointmentService appointmentService;
    private final BarberServiceService barberServiceService;
    private final NotificationService notificationService;

    public AppointmentController(UserService userService, AppointmentService appointmentService, BarberServiceService barberServiceService, NotificationService notificationService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.barberServiceService = barberServiceService;
        this.notificationService = notificationService;
    }


    @GetMapping("/booking")
    public ModelAndView bookingPage(
            @RequestParam(value = "barberId", required = false) UUID barberId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user  = userService.getById(authenticationDetails.getId());


        List<User> barbers = userService.findByUserRole();


        Map<String, Boolean> slots = new LinkedHashMap<>();

        if (barberId != null && date != null) {
            slots = appointmentService.getAvailableSlots(date, barberId);
        }

        List<BarberService> services = barberServiceService.getServicesByBarberId(barberId);

        ModelAndView modelAndView = new ModelAndView("booking");
        modelAndView.addObject("barbers", barbers);
        modelAndView.addObject("slots", slots);
        modelAndView.addObject("date", date);
        modelAndView.addObject("user", user);
        modelAndView.addObject("barberId", barberId);
        modelAndView.addObject("services",services);


        return modelAndView;
    }



    @PostMapping("/booking")
    public String bookAppointment(
            @RequestParam("timeSlot") String timeSlot,
            @RequestParam("barberId") UUID barberId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("serviceId") UUID serviceId,
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        UUID userId = authenticationDetails.getId();
        String customerName = authenticationDetails.getUsername();
        appointmentService.bookAppointment(customerName, date, timeSlot, userId, barberId, serviceId);

         return "redirect:/booking";
    }

    @DeleteMapping("/booking/cancel/{appointmentId}")
    @PreAuthorize("hasRole('USER')")
    public String cancelAppointment(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                    @PathVariable("appointmentId") UUID appointmentId) {

        UUID userId = authenticationDetails.getId();
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        appointmentService.cancelAppointmentByUserId(userId, appointmentId);
        notificationService.deleteEmailNotification(userId, appointment.getAppointmentDate(),appointment.getTimeSlot());

        return "redirect:/profile";
    }

    @DeleteMapping("/barber/cancel-appointment/{appointmentId}")
    @PreAuthorize("hasRole('BARBER')")
    public String cancelAppointmentByBarber(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                            @PathVariable("appointmentId") UUID appointmentId){

        UUID barberId = authenticationDetails.getId();
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        appointmentService.cancelAppointmentByBarberId(barberId,appointmentId);
        notificationService.deleteEmailNotificationByBarber(barberId, appointment.getAppointmentDate(),appointment.getTimeSlot());

        return "redirect:/booking";
    }

}

