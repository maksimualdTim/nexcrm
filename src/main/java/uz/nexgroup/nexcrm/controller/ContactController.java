package uz.nexgroup.nexcrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uz.nexgroup.nexcrm.mapper.ContactMapper;
import uz.nexgroup.nexcrm.model.Contact;
import uz.nexgroup.nexcrm.repository.ContactRepository;
import uz.nexgroup.nexcrm.request.ContactRequest;
import uz.nexgroup.nexcrm.response.ContactResponse;
import uz.nexgroup.nexcrm.service.ContactService;

import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("${v1API}/contacts")
public class ContactController {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactMapper contactMapper;

    @Autowired
    ContactService contactService;

    private static final int MAX_PAGE_SIZE = 250;
    
    @GetMapping
    public ResponseEntity<Page<ContactResponse>> getConatcts(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "50") int size) {
        
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Contact> contactsPage = contactRepository.findAll(pageable);

        List<ContactResponse> contactResponses = contactsPage.getContent().stream()
        .map(contactMapper::toResponse)
        .collect(Collectors.toList());

        Page<ContactResponse> contactResponsePage = new PageImpl<>(contactResponses, pageable, contactsPage.getTotalElements());

        return ResponseEntity.ok(contactResponsePage);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContact(@PathVariable Long id) {
        Contact contact = contactService.findById(id);

        return ResponseEntity.ok(contactMapper.toResponse(contact));
    }

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@RequestBody ContactRequest contactRequest) {
        Contact contact = contactMapper.toModel(contactRequest);
        contact = contactService.createContact(contact);
        
        return ResponseEntity.ok(contactMapper.toResponse(contact));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long id, @RequestBody ContactRequest contactRequest) {
        Contact contact = contactService.updateContact(id, contactRequest);
        return ResponseEntity.ok(contactMapper.toResponse(contact));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

}
