package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.BarberServiceCreate;
import bg.softuni.barberstudio.Web.Dto.BarberServiceEdit;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class ServiceController {

    private final UserService userService;
    private final BarberServiceService barberServiceService;

    public ServiceController(UserService userService, BarberServiceService barberServiceService) {
        this.userService = userService;
        this.barberServiceService = barberServiceService;
    }

    @GetMapping("/services")
    public ModelAndView getServices(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){

        List<User> barbers = userService.findByUserRole();

        ModelAndView modelAndView = new ModelAndView("services");
        modelAndView.addObject("barbers",barbers);


        return modelAndView;
    }

    @PostMapping("/barber-panel")
    @PreAuthorize("hasRole('BARBER')")
    public String addNewService(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid BarberServiceCreate barberServiceCreate, BindingResult bindingResult){

        User barber = userService.getById(authenticationDetails.getId());

        if(bindingResult.hasErrors()){
            return "redirect:/barber-panel";
        }

        barberServiceService.addNewService(barberServiceCreate, barber);

        return "redirect:/barber-panel";
    }

    @PutMapping("/barber-panel/edit-service/{serviceId}")
    @PreAuthorize("hasRole('BARBER')")
    public ModelAndView editBarberService(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid BarberServiceEdit barberServiceEdit, @PathVariable("serviceId") UUID serviceId, BindingResult bindingResult){

        User barber = userService.getById(authenticationDetails.getId());


        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("barber-panel");
            modelAndView.addObject("barberServiceEdit",barberServiceEdit);
            return modelAndView;
        }

        barberServiceService.editBarberServiceDetail(barberServiceEdit,serviceId);

        return new ModelAndView("redirect:/barber-panel");
    }

    @DeleteMapping("/barber-panel/delete-service/{serviceId}")
    @PreAuthorize("hasRole('BARBER')")
    public String deleteBarberService(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable("serviceId") UUID serviceId) {

        User barber = userService.getById(authenticationDetails.getId());

        barberServiceService.deleteBarberServiceById(serviceId);

        return "redirect:/barber-panel";
    }
}
