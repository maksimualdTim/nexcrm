package uz.nexgroup.nexcrm.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import uz.nexgroup.nexcrm.model.Company;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.repository.CompanyRepository;
import uz.nexgroup.nexcrm.request.CompanyRequest;

@Service
public class CompanyService {
    @Autowired
    UserService userService;

    @Autowired
    CompanyRepository companyRepository;

    public Company findById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company with id: " + id + " not found"));
    }

    public Company createCompany(Company contact) {
        return companyRepository.save(contact);
    }

    public Company updateCompany (Long id, CompanyRequest companyRequest) {
        Company company = findById(id);

        if(companyRequest.getName() != null) {
            company.setName(companyRequest.getName());
        }

        if(companyRequest.getResponsibleId() != null) {
            User user = userService.findById(companyRequest.getResponsibleId());
            company.setResponsible(user);
        }

        if(companyRequest.getUpdatedAt() != null) {
            company.setUpdatedAt(companyRequest.getUpdatedAt());
        } else {
            company.setUpdatedAt(LocalDateTime.now());
        }

        Long updatedBy = companyRequest.getUpdatedById() == null ? userService.getCurrentUser().getId() : companyRequest.getUpdatedById();

        User updatedByUser = userService.findById(updatedBy);
        company.setUpdatedBy(updatedByUser);
        
        if(companyRequest.getCreatedById() != null) {
            User createdByUser = userService.findById(updatedBy);
            company.setCreatedBy(createdByUser);
        }

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        Company contact = findById(id);
        companyRepository.delete(contact);
    }
}
