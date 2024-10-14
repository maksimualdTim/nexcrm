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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.nexgroup.nexcrm.service.LeadService;
import uz.nexgroup.nexcrm.service.PipelineService;
import uz.nexgroup.nexcrm.service.StatusService;
import jakarta.validation.Valid;
import uz.nexgroup.nexcrm.mapper.LeadMapper;
import uz.nexgroup.nexcrm.mapper.PipelineMapper;
import uz.nexgroup.nexcrm.mapper.StatusMapper;
import uz.nexgroup.nexcrm.model.Lead;
import uz.nexgroup.nexcrm.model.Pipeline;
import uz.nexgroup.nexcrm.model.Status;
import uz.nexgroup.nexcrm.repository.LeadRepository;
import uz.nexgroup.nexcrm.request.LeadRequest;
import uz.nexgroup.nexcrm.request.PipelineRequest;
import uz.nexgroup.nexcrm.request.StatusRequest;
import uz.nexgroup.nexcrm.response.LeadResponse;
import uz.nexgroup.nexcrm.response.PipelineResponse;
import uz.nexgroup.nexcrm.response.StatusResponse;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("${v1API}/leads")
public class LeadController {
    private static final int MAX_PAGE_SIZE = 250;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    LeadMapper leadMapper;

    @Autowired
    LeadService leadService;

    @Autowired
    PipelineService pipelineService;

    @Autowired
    PipelineMapper pipelineMapper;

    @Autowired
    StatusMapper statusMapper;

    @Autowired
    StatusService statusService;

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
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody LeadRequest leadRequest) {
        Lead lead = leadMapper.toModel(leadRequest);
        lead = leadRepository.save(lead);

        return ResponseEntity.ok(leadMapper.toResponse(lead));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LeadResponse> updateLead(@PathVariable Long id, @RequestBody LeadRequest leadRequest) {
        Lead lead = leadService.updateLead(id, leadRequest);

        return ResponseEntity.ok(leadMapper.toResponse(lead));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/pipelines")
    public ResponseEntity<List<PipelineResponse>> getPipelines() {
        List<Pipeline> pipelines = pipelineService.getPipelines();
        List<PipelineResponse> pipelineResponses= pipelineMapper.toResponse(pipelines);
        return ResponseEntity.ok(pipelineResponses);
    }

    @PostMapping("/pipelines")
    public ResponseEntity<PipelineResponse> createPipeline(@RequestBody PipelineRequest pipelineRequest) {
        Pipeline pipeline = pipelineService.createPipeline(pipelineRequest);
        
        return ResponseEntity.ok(pipelineMapper.toResponse(pipeline));
    }
    
    
    @GetMapping("/pipelines/{id}")
    public ResponseEntity<PipelineResponse> getPipeline(@PathVariable Long id) {
        Pipeline pipeline = pipelineService.findById(id);
        return ResponseEntity.ok(pipelineMapper.toResponse(pipeline));
    }

    @PostMapping("/pipelines/{id}/statuses")
    public ResponseEntity<StatusResponse> createStatus(@PathVariable Long id, @RequestBody StatusRequest statusRequest) {
        Status status = statusMapper.toModel(statusRequest);
        Pipeline pipeline = pipelineService.findById(id);
        pipelineService.addStatus(pipeline, status);

        return ResponseEntity.ok(statusMapper.toResponse(status));
    }

    @PutMapping("pipelines/{pipelineId}/statuses/{statusId}")
    public ResponseEntity<StatusResponse> putMethodName(@PathVariable Long pipelineId, @PathVariable Long statusId, @RequestBody StatusRequest statusRequest) {
        Status status = statusMapper.toModel(statusRequest);
        status = statusService.createStatus(status);
        return ResponseEntity.ok(statusMapper.toResponse(status));
    }


    @PutMapping("pipelines/{id}")
    public ResponseEntity<PipelineResponse> updatePipeline(@PathVariable Long id, @RequestBody PipelineRequest pipelineRequest) {
        Pipeline pipeline = pipelineService.updatePipeline(id, pipelineRequest);
        
        return ResponseEntity.ok(pipelineMapper.toResponse(pipeline));
    }

    @DeleteMapping("/pipelines/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable Long id) {
        pipelineService.deletePipeline(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pipelines/{pipelineId}/statuses/{statusId}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long pipelineId, @PathVariable Long statusId) {
        pipelineService.deleteStatus(pipelineId, statusId);
        return ResponseEntity.noContent().build();
    }
    
}
