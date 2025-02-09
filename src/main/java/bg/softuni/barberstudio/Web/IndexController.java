package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.LoginRequest;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    public String getIndexPage(){

        return "index";
    }


    @GetMapping("/login")
    public ModelAndView getLoginPage(){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session){

        if(bindingResult.hasErrors()){
            return new ModelAndView("login");
        }

        //TODO:

        return new ModelAndView("redirect:/home");
    }


    @GetMapping("/register-user")
    public ModelAndView getRegisterPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register-user");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register-user")
    public ModelAndView registerUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult){

       if(bindingResult.hasErrors()){
           return new ModelAndView("register-user");
       }

       userService.register(registerRequest);


        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/register-master")
    public String getRegisterMasterPage(){

        return "register-master";
    }
}
