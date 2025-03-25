package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Product.Model.Product;
import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.ProductOrder.Service.ProductOrderService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.ProductOrderRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class ProductOrderController {

    private final ProductOrderService productOrderService;
    private final UserService userService;
    private final ProductService productService;


    public ProductOrderController(ProductOrderService productOrderService, UserService userService, ProductService productService) {
        this.productOrderService = productOrderService;
        this.userService = userService;
        this.productService = productService;
    }


    @PostMapping("/barber/{id}/buy-product/{productId}")
    public String buyProduct(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, RedirectAttributes redirectAttributes, @PathVariable("productId") UUID productId,
                             @Valid ProductOrderRequest productOrderRequest, BindingResult bindingResult, @PathVariable("id") UUID barberId) {

        User buyer = userService.getById(authenticationDetails.getId());
        Product product = productService.getById(productId);

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/barber/" + barberId;
        }

        productOrderService.createOrderProduct(buyer,product,productOrderRequest);
        redirectAttributes.addFlashAttribute("message", "Product purchased successfully!");

        return "redirect:/barber/" + barberId;
    }

    @DeleteMapping("/profile/cancel-order/{productOrderId}")
    public String cancelOrder(@PathVariable("productOrderId") UUID productOrderId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        productOrderService.cancelProductOrder(productOrderId);

        return "redirect:/profile";
    }

    @DeleteMapping("/barber/cancel-order/{productOrderId}")
    @PreAuthorize("hasRole('BARBER')")
    public String cancelOrderByBarber(@PathVariable("productOrderId") UUID productOrderId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        productOrderService.cancelProductOrder(productOrderId);

        return "redirect:/profile";
    }
}
