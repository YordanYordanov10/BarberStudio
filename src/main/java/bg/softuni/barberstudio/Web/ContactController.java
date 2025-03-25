package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Contact.Model.Contact;
import bg.softuni.barberstudio.Web.Dto.ContactRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactController {

    @GetMapping("/contact-us")
    public ModelAndView contactUs() {

        ModelAndView modelAndView = new ModelAndView("contact-us");
        modelAndView.addObject("contactRequest", new ContactRequest());

        return modelAndView;
    }
}
