package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.ContactRequest;
import bg.softuni.barberstudio.Web.Dto.LoginRequest;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class IndexController {

    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public IndexController(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        List<User> barbers = userService.findByUserRole();
        List<Comment> testimonials = commentService.getAllComments();

        ModelAndView modelAndView = new ModelAndView("/index");
        modelAndView.addObject("barbers", barbers);
        modelAndView.addObject("testimonials", testimonials);

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


        if (!registerRequest.isPasswordsMatch()) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords must match.");
        }

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("registerRequest", registerRequest);
            modelAndView.addObject("org.springframework.validation.BindingResult.registerRequest", bindingResult);
            return modelAndView;
        }

        userService.register(registerRequest);
        return new ModelAndView("redirect:/login");
    }

}
