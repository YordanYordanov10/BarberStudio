package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.LoginRequest;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        ModelAndView modelAndView = new ModelAndView("/index");

        if (authenticationDetails != null) {
            User user = userService.getById(authenticationDetails.getId());
            modelAndView.addObject("user", user);
            modelAndView.addObject("isLoggedIn", true);
        } else {
            modelAndView.addObject("isLoggedIn", false);
        }
        return modelAndView;
    }


    @GetMapping("/login")
    public ModelAndView getLoginPage(){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }



    @GetMapping("/register")
    public ModelAndView getRegisterPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                                     BindingResult bindingResult) {

        // Проверка дали паролите съвпадат
        if (!registerRequest.isPasswordsMatch()) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords must match.");
        }

        // Ако има грешки, върни същата страница със запазени данни и грешки
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("registerRequest", registerRequest);
            modelAndView.addObject("org.springframework.validation.BindingResult.registerRequest", bindingResult);
            return modelAndView;
        }

        // Ако няма грешки, регистрирай потребителя
        userService.register(registerRequest);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/contact")
    public ModelAndView getContactPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("contact");

        return modelAndView;
    }

    @GetMapping("/services")
    public ModelAndView getServicesPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("services");

        return modelAndView;
    }



}
