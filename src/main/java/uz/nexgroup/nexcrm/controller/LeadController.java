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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uz.nexgroup.nexcrm.mapper.LeadMapper;
import uz.nexgroup.nexcrm.model.Lead;
import uz.nexgroup.nexcrm.repository.LeadRepository;
import uz.nexgroup.nexcrm.response.LeadResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${v1API}/leads")
public class LeadController {
    private static final int MAX_PAGE_SIZE = 250;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    LeadMapper leadMapper;

    @GetMapping
    public ResponseEntity<Page<LeadResponse>> getLeads(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "50") int size) {
        
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Lead> leadsPage = leadRepository.findAll(pageable);

        List<LeadResponse> leadResponses = leadsPage.getContent().stream()
            .map(leadMapper::toResponse)
            .collect(Collectors.toList());

        Page<LeadResponse> leadResponsePage = new PageImpl<>(leadResponses, pageable, leadsPage.getTotalElements());

        return ResponseEntity.ok(leadResponsePage);
    }


    @PostMapping
    public ResponseEntity createLead() {

    }

    @PatchMapping
    public ResponseEntity updateLead() {}

    @DeleteMapping
    public ResponseEntity deleteLead() {}
}
