package uz.nexgroup.nexcrm.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uz.nexgroup.nexcrm.mapper.CompanyMapper;
import uz.nexgroup.nexcrm.model.Company;
import uz.nexgroup.nexcrm.model.Contact;
import uz.nexgroup.nexcrm.repository.CompanyRepository;
import uz.nexgroup.nexcrm.request.CompanyRequest;
import uz.nexgroup.nexcrm.request.ContactRequest;
import uz.nexgroup.nexcrm.response.CompanyResponse;
import uz.nexgroup.nexcrm.response.ContactResponse;
import uz.nexgroup.nexcrm.service.CompanyService;

@RestController
@RequestMapping("${v1API}/companies")
public class CompanyController {
    private static final int MAX_PAGE_SIZE = 250;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    CompanyService companyService;
    
    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> getCompanies(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "50") int size) {
        
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Company> contactsPage = companyRepository.findAll(pageable);

        List<CompanyResponse> contactResponses = contactsPage.getContent().stream()
        .map(companyMapper::toResponse)
        .collect(Collectors.toList());

        Page<CompanyResponse> contactResponsePage = new PageImpl<>(contactResponses, pageable, contactsPage.getTotalElements());

        return ResponseEntity.ok(contactResponsePage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long id) {
        Company company = companyService.findById(id);

        return ResponseEntity.ok(companyMapper.toResponse(company));
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest companyRequest) {
        Company company = companyMapper.toModel(companyRequest);
        company = companyService.createCompany(company);
        
        return ResponseEntity.ok(companyMapper.toResponse(company));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody CompanyRequest companyRequest) {
        Company company = companyService.updateCompany(id, companyRequest);
        return ResponseEntity.ok(companyMapper.toResponse(company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
