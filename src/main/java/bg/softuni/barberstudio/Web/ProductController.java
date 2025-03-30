package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Product.Service.ProductService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.BarberCreateProduct;
import bg.softuni.barberstudio.Web.Dto.BarberEditProduct;
import bg.softuni.barberstudio.Web.Dto.BarberServiceEdit;
import bg.softuni.barberstudio.Web.Dto.ProductOrderRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

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
    public String addProduct(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                             @Valid BarberCreateProduct barberCreateProduct,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        User barber = userService.getById(authenticationDetails.getId());

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("barberCreateProduct", barberCreateProduct);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.barberCreateProduct", bindingResult);
            return "redirect:/barber-panel";
        }

        try {
            productService.createNewProduct(barberCreateProduct, barber);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add product: " + e.getMessage());
        }

        return "redirect:/barber-panel";
    }


    @PutMapping("/barber-panel/edit-product/{productId}")
    @PreAuthorize("hasRole('BARBER')")
    public ModelAndView editBarberProduct(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid BarberEditProduct barberEditProduct, @PathVariable("productId") UUID productId, BindingResult bindingResult){

        User barber = userService.getById(authenticationDetails.getId());


        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("barber-panel");
            modelAndView.addObject("barberEditProduct",barberEditProduct);
            return modelAndView;
        }
        productService.editBarberProductDetail(barberEditProduct,productId);

        return new ModelAndView("redirect:/barber-panel");
    }

    @DeleteMapping("/barber-panel/delete-product/{productId}")
    @PreAuthorize("hasRole('BARBER')")
    public String deleteProduct(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable("productId") UUID productId) {

        User barber = userService.getById(authenticationDetails.getId());

        productService.deleteProductByProductId(productId);

        return "redirect:/barber-panel";
    }


}

