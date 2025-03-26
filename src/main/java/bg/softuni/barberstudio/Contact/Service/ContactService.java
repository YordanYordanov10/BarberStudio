package bg.softuni.barberstudio.Contact.Service;

import bg.softuni.barberstudio.Contact.Model.Contact;
import bg.softuni.barberstudio.Contact.Repository.ContactRepository;
import bg.softuni.barberstudio.Web.Dto.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


    public void createContact(ContactRequest contactRequest) {

        Contact contact = Contact.builder()
                .name(contactRequest.getName())
                .email(contactRequest.getEmail())
                .message(contactRequest.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        contactRepository.save(contact);
    }

    public List<Contact> getAllContact() {
        return contactRepository.findAll();
    }
}
