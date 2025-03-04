package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Barber.Service.BarberService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AppointmentController {

    private final UserService userService;

    public AppointmentController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/booking")
    public ModelAndView bookingPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {


        List<User> barbers = userService.findByUserRole();

        ModelAndView modelAndView = new ModelAndView("booking");
        modelAndView.addObject("barbers", barbers);

        return modelAndView;
    }
}
