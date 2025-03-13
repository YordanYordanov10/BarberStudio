package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.BarberCreateProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/barber-panel/add-product")
    @PreAuthorize("hasRole('BARBER')")
    public String addProduct(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid BarberCreateProduct barberCreateProduct, BindingResult bindingResult) {

        User barber = userService.getById(authenticationDetails.getId());

        if(bindingResult.hasErrors()){
            return "redirect:/barber-panel";
        }

        productService.createNewProduct(barberCreateProduct, barber);

        return "redirect:/barber-panel";
    }
}

