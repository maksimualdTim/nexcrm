package uz.nexgroup.nexcrm.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import uz.nexgroup.nexcrm.model.Lead;
import uz.nexgroup.nexcrm.response.LeadResponse;
import uz.nexgroup.nexcrm.service.UserService;

public class LeadMapper {

    @Autowired
    UserService userService;

    public LeadResponse toResponse(Lead lead) {
        LeadResponse leadResponse = new LeadResponse();

        leadResponse.setAccountId(userService.getCurrentUser().getAccount().getId());
        leadResponse.setCreatedAt(lead.getCreatedAt());
        leadResponse.setUpdatedAt(lead.getUpdatedAt());
        leadResponse.setCreatedBy(lead.getCreatedBy().getId());
        leadResponse.setUpdatedBy(lead.getUpdatedBy() != null ? lead.getUpdatedBy().getId() : null);
        leadResponse.setId(lead.getId());
        leadResponse.setName(lead.getName());
        leadResponse.setPipelineId(lead.getStatus().getPipeline().getId());
        leadResponse.setStatusId(lead.getStatus().getId());
        leadResponse.setResponsibleId(lead.getResponsible().getId());
        return leadResponse;
    }
}
