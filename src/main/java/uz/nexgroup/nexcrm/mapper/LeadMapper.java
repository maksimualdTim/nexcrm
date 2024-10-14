package uz.nexgroup.nexcrm.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uz.nexgroup.nexcrm.model.Lead;
import uz.nexgroup.nexcrm.model.Status;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.request.LeadRequest;
import uz.nexgroup.nexcrm.response.LeadResponse;
import uz.nexgroup.nexcrm.service.StatusService;
import uz.nexgroup.nexcrm.service.UserService;
import java.time.LocalDateTime;

@Component
public class LeadMapper {

    @Autowired
    UserService userService;

    @Autowired
    StatusService statusService;

    public LeadResponse toResponse(Lead lead) {
        LeadResponse leadResponse = new LeadResponse();

        leadResponse.setAccountId(userService.getCurrentUser().getAccount().getId());
        leadResponse.setCreatedAt(lead.getCreatedAt());
        leadResponse.setUpdatedAt(lead.getUpdatedAt());
        leadResponse.setCreatedById(lead.getCreatedBy().getId());
        leadResponse.setUpdatedById(lead.getUpdatedBy() != null ? lead.getUpdatedBy().getId() : null);
        leadResponse.setId(lead.getId());
        leadResponse.setName(lead.getName());
        leadResponse.setPipelineId(lead.getStatus().getPipeline().getId());
        leadResponse.setStatusId(lead.getStatus().getId());
        leadResponse.setResponsibleId(lead.getResponsible().getId());
        return leadResponse;
    }

    public Lead toModel(LeadRequest leadRequest) {
        Lead lead = new Lead();

        lead.setAccount(userService.getCurrentUser().getAccount());
        lead.setName(leadRequest.getName());
        lead.setPrice(leadRequest.getPrice());
        lead.setCreatedAt(leadRequest.getCreatedAt() == null ? LocalDateTime.now() : leadRequest.getCreatedAt());
        lead.setUpdatedAt(leadRequest.getUpdatedAt() == null ? LocalDateTime.now() : leadRequest.getUpdatedAt());
        
        Long userId = leadRequest.getCreatedById() != null ? leadRequest.getCreatedById() : userService.getCurrentUser().getId();
        User createdBy = userService.findById(userId);
        lead.setCreatedBy(createdBy);

        if(leadRequest.getUpdatedById() != null) {
            User updatedBy = userService.findById(leadRequest.getUpdatedById());
            lead.setUpdatedBy(updatedBy);
        }

        if(leadRequest.getResponsibleId() != null) {
            User responsible = userService.findById(leadRequest.getResponsibleId());
            lead.setResponsible(responsible);
        }
        
        if(leadRequest.getStatusId() != null) {
            Status status = statusService.findById(leadRequest.getStatusId());
            lead.setStatus(status);
        }
        

        return lead;
    }
}
