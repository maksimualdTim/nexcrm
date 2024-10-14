package uz.nexgroup.nexcrm.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uz.nexgroup.nexcrm.model.Contact;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.request.ContactRequest;
import uz.nexgroup.nexcrm.response.ContactResponse;
import uz.nexgroup.nexcrm.service.UserService;
import java.time.LocalDateTime;

@Component
public class ContactMapper {
    @Autowired
    UserService userService;

    public ContactResponse toResponse(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();

        contactResponse.setId(contact.getId());
        contactResponse.setCreatedAt(contact.getCreatedAt());
        contactResponse.setCreatedById(contact.getCreatedBy().getId());
        contactResponse.setUpdatedById(contact.getUpdatedBy() == null ? null : contact.getUpdatedBy().getId());
        contactResponse.setUpdatedAt(contact.getUpdatedAt());
        contactResponse.setAccountId(contact.getAccount().getId());
        contactResponse.setName(contact.getName());
        return contactResponse;
    }
    
    public Contact toModel(ContactRequest contactRequest) {
        Contact contact = new Contact();

        contact.setAccount(userService.getCurrentUser().getAccount());
        contact.setName(contactRequest.getName());
        contact.setCreatedAt(contactRequest.getCreatedAt() == null ? LocalDateTime.now() : contactRequest.getCreatedAt());
        contact.setUpdatedAt(contactRequest.getUpdatedAt());

        Long createdById = contactRequest.getCreatedById() == null ? userService.getCurrentUser().getId() : contactRequest.getCreatedById();

        User user = userService.findById(createdById);

        contact.setCreatedBy(user);

        if(contactRequest.getUpdatedById() == null) {
            User updatedBy = userService.findById(contactRequest.getUpdatedById());
            contact.setUpdatedBy(updatedBy);
        }
        
        Long responsibleId = contactRequest.getResponsibleId() == null ? userService.getCurrentUser().getId() : contactRequest.getResponsibleId();
        User responsible = userService.findById(responsibleId);
        
        contact.setResponsible(responsible);
        return contact;
    }
}
