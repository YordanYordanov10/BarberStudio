package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Contact.Model.Contact;
import bg.softuni.barberstudio.Contact.Service.ContactService;
import bg.softuni.barberstudio.Web.Dto.ContactRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public ModelAndView getContactUs() {

        ModelAndView modelAndView = new ModelAndView("contact");
        modelAndView.addObject("contactRequest", new ContactRequest());

        return modelAndView;
    }

    @PostMapping("/contact")
    public ModelAndView createContactUs(@Valid ContactRequest contactRequest, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("contact");
            modelAndView.addObject("contactRequest", contactRequest);
            modelAndView.addObject("errors", bindingResult);
            return modelAndView;
        }

        contactService.createContact(contactRequest);
        return new ModelAndView("redirect:/");
    }

}
