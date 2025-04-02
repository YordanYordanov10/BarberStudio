package bg.softuni.barberstudio.web;

import bg.softuni.barberstudio.Contact.Service.ContactService;
import bg.softuni.barberstudio.Web.ContactController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
public class ContactControllerApiTest {

        @Autowired
        MockMvc mockMvc;

        @MockitoBean
        ContactService contactService;


        @Test
        void getRequestToContactPage_shouldReturnContactHtml() throws Exception {

            MockHttpServletRequestBuilder request = get("/contact");

            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("contact"))
                    .andExpect(model().attributeExists("contactRequest"));
        }


    }

