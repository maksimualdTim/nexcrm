package uz.nexgroup.nexcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import uz.nexgroup.nexcrm.model.Contact;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.repository.ContactRepository;
import uz.nexgroup.nexcrm.request.ContactRequest;
import java.time.LocalDateTime;

@Service
public class ContactService {

    @Autowired
    UserService userService;

    @Autowired
    ContactRepository contactRepository;

    public Contact findById(Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contact with id: " + id + " not found"));
    }

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact updateContact (Long id, ContactRequest contactRequest) {
        Contact contact = findById(id);

        if(contactRequest.getName() != null) {
            contact.setName(contactRequest.getName());
        }

        if(contactRequest.getResponsibleId() != null) {
            User user = userService.findById(contactRequest.getResponsibleId());
            contact.setResponsible(user);
        }

        if(contactRequest.getUpdatedAt() != null) {
            contact.setUpdatedAt(contactRequest.getUpdatedAt());
        } else {
            contact.setUpdatedAt(LocalDateTime.now());
        }

        Long updatedBy = contactRequest.getUpdatedById() == null ? userService.getCurrentUser().getId() : contactRequest.getUpdatedById();

        User updatedByUser = userService.findById(updatedBy);
        contact.setUpdatedBy(updatedByUser);
        
        if(contactRequest.getCreatedById() != null) {
            User createdByUser = userService.findById(updatedBy);
            contact.setCreatedBy(createdByUser);
        }

        return contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        Contact contact = findById(id);
        contactRepository.delete(contact);
    }
}
