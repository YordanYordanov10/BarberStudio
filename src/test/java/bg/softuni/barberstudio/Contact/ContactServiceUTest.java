package bg.softuni.barberstudio.Contact;

import bg.softuni.barberstudio.Contact.Model.Contact;
import bg.softuni.barberstudio.Contact.Repository.ContactRepository;
import bg.softuni.barberstudio.Contact.Service.ContactService;
import bg.softuni.barberstudio.Web.Dto.ContactRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceUTest {


    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;




    @Test
    void CreateContactAndSave() {

        ContactRequest contactRequest = ContactRequest.builder()
                .name("daka123")
                .email("daka@gmail.com")
                .message("Lorem Ipsum")
                .build();

        Contact mockContact = Contact.builder()
                .id(UUID.randomUUID())
                .name(contactRequest.getName())
                .email(contactRequest.getEmail())
                .message(contactRequest.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        when(contactRepository.save(any(Contact.class))).thenReturn(mockContact);

        Contact createdContact = contactService.createContact(contactRequest);


        assertNotNull(createdContact);
        assertEquals(contactRequest.getName(), createdContact.getName());
        assertEquals(contactRequest.getEmail(), createdContact.getEmail());
        assertEquals(contactRequest.getMessage(), createdContact.getMessage());
        assertNotNull(createdContact.getCreatedAt());

        verify(contactRepository, times(1)).save(any(Contact.class));

    }

    @Test
    void givenExistingContactsInDatabase_whenGetAllContact_thenReturnThemAll(){

        List<Contact> contacts = List.of(new Contact(),new Contact());
        when(contactRepository.findAll()).thenReturn(contacts);

        List<Contact> allContact = contactService.getAllContact();

        assertEquals(allContact.size(), 2);
    }

}
