package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

    public AppointmentController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
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

        ModelAndView modelAndView = new ModelAndView("booking");
        modelAndView.addObject("barbers", barbers);
        modelAndView.addObject("slots", slots);
        modelAndView.addObject("date", date);
        modelAndView.addObject("user", user);
        modelAndView.addObject("barberId", barberId);

        return modelAndView;
    }

    @PostMapping("/booking")
    public String bookAppointment(
            @RequestParam("timeSlot") String timeSlot,
            @RequestParam("barberId") UUID barberId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        UUID userId = authenticationDetails.getId();
        String customerName = authenticationDetails.getUsername();
        appointmentService.bookAppointment(customerName, date, timeSlot, userId, barberId);
         return "redirect:/booking";
    }

    @DeleteMapping("/booking/cancel/{appointmentId}")
    public String cancelAppointment(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                    @PathVariable("appointmentId") UUID appointmentId) {

        UUID userId = authenticationDetails.getId();
        appointmentService.cancelAppointmentByUserId(userId, appointmentId);

        return "redirect:/profile";
    }

}

