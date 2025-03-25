package bg.softuni.barberstudio.Contact.Repository;

import bg.softuni.barberstudio.Contact.Model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
}
