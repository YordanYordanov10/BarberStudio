package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.UserEditRequest;
import bg.softuni.barberstudio.untility.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){

        User user = userService.getById(authenticationDetails.getId());

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userEditRequest", DtoMapper.mapUserToUserEditRequest(user));


        return modelAndView;
    }

    @PutMapping("/profile")
    public ModelAndView editProfile(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid UserEditRequest userEditRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            User user = userService.getById(authenticationDetails.getId());
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("userEditRequest", userEditRequest);
            return modelAndView;
        }


        userService.editUserDetails(authenticationDetails.getId(), userEditRequest);

        return new ModelAndView("redirect:/profile");

    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){

        User user  = userService.getById(authenticationDetails.getId());
        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView("admin-panel");
        modelAndView.addObject("user", user);
        modelAndView.addObject("users",users);
        modelAndView.addObject("roles", UserRole.values());


        return modelAndView;
    }

    @PostMapping("/users/changeRole")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserRole(@RequestParam UUID userId, @RequestParam String role){

        userService.updateUserRole(userId,role);

        return "redirect:/users";
    }

    @PostMapping("/users/changeStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserStatus(@RequestParam UUID userId){

        userService.updateUserStatus(userId);

        return "redirect:/users";
    }


}
