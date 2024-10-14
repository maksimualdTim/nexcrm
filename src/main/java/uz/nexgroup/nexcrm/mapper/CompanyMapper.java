package uz.nexgroup.nexcrm.mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uz.nexgroup.nexcrm.model.Company;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.request.CompanyRequest;
import uz.nexgroup.nexcrm.response.CompanyResponse;
import uz.nexgroup.nexcrm.service.UserService;

@Component
public class CompanyMapper {
    @Autowired
    UserService userService;

    public CompanyResponse toResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();

        companyResponse.setId(company.getId());
        companyResponse.setCreatedAt(company.getCreatedAt());
        companyResponse.setCreatedById(company.getCreatedBy().getId());
        companyResponse.setUpdatedById(company.getUpdatedBy() == null ? null : company.getUpdatedBy().getId());
        companyResponse.setUpdatedAt(company.getUpdatedAt());
        companyResponse.setAccountId(company.getAccount().getId());
        companyResponse.setName(company.getName());
        return companyResponse;
    }
    
    public Company toModel(CompanyRequest companyRequest) {
        Company company = new Company();

        company.setAccount(userService.getCurrentUser().getAccount());
        company.setName(companyRequest.getName());
        company.setCreatedAt(companyRequest.getCreatedAt() == null ? LocalDateTime.now() : companyRequest.getCreatedAt());
        company.setUpdatedAt(companyRequest.getUpdatedAt());

        Long createdById = companyRequest.getCreatedById() == null ? userService.getCurrentUser().getId() : companyRequest.getCreatedById();

        User user = userService.findById(createdById);

        company.setCreatedBy(user);

        if(companyRequest.getUpdatedById() == null) {
            User updatedBy = userService.findById(companyRequest.getUpdatedById());
            company.setUpdatedBy(updatedBy);
        }
        
        Long responsibleId = companyRequest.getResponsibleId() == null ? userService.getCurrentUser().getId() : companyRequest.getResponsibleId();
        User responsible = userService.findById(responsibleId);
        
        company.setResponsible(responsible);
        return company;
    }
}
