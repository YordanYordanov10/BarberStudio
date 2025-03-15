package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Appointment.Model.Appointment;
import bg.softuni.barberstudio.Appointment.Service.AppointmentService;
import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.ProductOrder.Model.ProductOrder;
import bg.softuni.barberstudio.ProductOrder.Service.ProductOrderService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.Service.Model.BarberService;
import bg.softuni.barberstudio.Service.Service.BarberServiceService;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.*;
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
    private final AppointmentService appointmentService;
    private final BarberServiceService barberServiceService;
    private final CommentService commentService;
    private final ProductService productService;
    private final ProductOrderService productOrderService;

    @Autowired
    public UserController(UserService userService, AppointmentService appointmentService, BarberServiceService barberServiceService, CommentService commentService, ProductService productService, ProductOrderService productOrderService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.barberServiceService = barberServiceService;
        this.commentService = commentService;
        this.productService = productService;
        this.productOrderService = productOrderService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){

        User user = userService.getById(authenticationDetails.getId());

        List<Appointment> appointments = appointmentService.getAppointmentsByUser(authenticationDetails.getId());
        List<ProductOrder> orders = productOrderService.getProductOrderByUserId(user);

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userEditRequest", DtoMapper.mapUserToUserEditRequest(user));
        modelAndView.addObject("appointments", appointments);
        modelAndView.addObject("orders", orders);


        return modelAndView;
    }

    @PutMapping("/profile")
    public ModelAndView editProfile(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid UserEditRequest userEditRequest, BindingResult bindingResult){

        User user = userService.getById(authenticationDetails.getId());


        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("userEditRequest", userEditRequest);
            return modelAndView;
        }


        userService.editUserDetails(authenticationDetails.getId(), userEditRequest);

        return new ModelAndView("redirect:/profile");

    }

    @GetMapping("/barber-panel")
    @PreAuthorize("hasRole('BARBER')")
    public ModelAndView getBarberPanel(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){

        User user  = userService.getById(authenticationDetails.getId());

        List<BarberService> services = barberServiceService.getAllServicesByAddedByBarber(user);
        List<Product> products = productService.getAllProductsByAddedByBarber(user);


        ModelAndView modelAndView = new ModelAndView("barber-panel");
        modelAndView.addObject("user",user);
        modelAndView.addObject("barberServiceCreate", new BarberServiceCreate());
        modelAndView.addObject("barberCreateProduct", new BarberCreateProduct());
        modelAndView.addObject("barberServiceEdit", new BarberServiceEdit());
        modelAndView.addObject("barberEditProduct", new BarberEditProduct());
        modelAndView.addObject("services",services);
        modelAndView.addObject("products",products);



        return modelAndView;
    }

    @GetMapping("/barber/{id}")
    public ModelAndView getBarberPage(@PathVariable("id") UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getId());
        User barber = userService.getById(id);

        List<BarberService> services = barberServiceService.getAllServices(id);
        List<Comment> comments = commentService.getAllComments();
        List<Product> products = productService.getAllProducts(barber);

        ModelAndView modelAndView = new ModelAndView("barber");
        modelAndView.addObject("user",user);
        modelAndView.addObject("barber", barber);
        modelAndView.addObject("services", services);
        modelAndView.addObject("commentCreateRequest", new CommentCreateRequest());
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("productOrderRequest", new ProductOrderRequest());
        modelAndView.addObject("products", products);



        return modelAndView;
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
