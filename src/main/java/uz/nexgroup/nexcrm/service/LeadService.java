package uz.nexgroup.nexcrm.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import uz.nexgroup.nexcrm.model.Lead;
import uz.nexgroup.nexcrm.model.Status;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.repository.LeadRepository;
import uz.nexgroup.nexcrm.request.LeadRequest;

@Service
public class LeadService {

    @Autowired
    StatusService statusService;

    @Autowired
    PipelineService pipelineService;

    @Autowired
    UserService userService;

    @Autowired
    LeadRepository leadRepository;

    public Lead updateLead(Long id, LeadRequest leadRequest) {
        Lead lead = leadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lead with id: " + id + " not found"));

        if(leadRequest.getName() != null) {
            lead.setName(leadRequest.getName());
        }

        if(leadRequest.getPrice() != null) {
            lead.setPrice(leadRequest.getPrice());
        }

        if(leadRequest.getCreatedAt() != null) {
            lead.setCreatedAt(leadRequest.getCreatedAt());
        }

        if(leadRequest.getStatusId() != null) {
            Status status = statusService.findById(id);

            lead.setStatus(status);
        }

        if(leadRequest.getUpdatedAt() != null) {
            lead.setUpdatedAt(leadRequest.getUpdatedAt());
        } else {
            lead.setUpdatedAt(LocalDateTime.now());
        }

        if(leadRequest.getCreatedById() != null) {
            User cretedBy = userService.findById(leadRequest.getCreatedById());
            lead.setCreatedBy(cretedBy);
        }

        Long userId = leadRequest.getUpdatedById() != null ? leadRequest.getUpdatedById() : userService.getCurrentUser().getId();
        User updatedBy = userService.findById(userId);
        lead.setUpdatedBy(updatedBy);

        if(leadRequest.getResponsibleId() != null) {
            User user = userService.findById(leadRequest.getResponsibleId());
            lead.setResponsible(user);
        }

        return leadRepository.save(lead);
    }

    public Lead findById(Long id) {
        return leadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lead with id: " + id + " not found"));
    }

    public void deleteLead(Long id) {
        Lead lead = findById(id);
        leadRepository.delete(lead);
    }
}
