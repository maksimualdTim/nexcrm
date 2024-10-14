package uz.nexgroup.nexcrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import uz.nexgroup.nexcrm.mapper.PipelineMapper;
import uz.nexgroup.nexcrm.model.Pipeline;
import uz.nexgroup.nexcrm.model.Status;
import uz.nexgroup.nexcrm.repository.PipelineRepository;
import uz.nexgroup.nexcrm.request.PipelineRequest;

@Service
public class PipelineService {
    @Autowired
    PipelineRepository pipelineRepository;

    @Autowired
    UserService userService;

    @Autowired
    PipelineMapper pipelineMapper;

    @Autowired
    StatusService statusService;

    public Pipeline findById(Long id) {
        return pipelineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pipeline with id: " + id + " not found"));
    }
    
    public List<Pipeline> getPipelines() {
        return pipelineRepository.findAllByAccount(userService.getCurrentUser().getAccount());
    }

    public void deletePipeline(Long id) {
        Pipeline pipeline = findById(id);
        pipelineRepository.delete(pipeline);
    }

    public Pipeline createPipeline(PipelineRequest pipelineRequest) {
        Pipeline pipeline = pipelineMapper.toModel(pipelineRequest);
        List<Status> statuses = pipeline.getStatuses();
        statuses = statusService.createStatus(statuses);

        return pipelineRepository.save(pipeline);
    }

    public Pipeline createPipeline(Pipeline pipeline) {
        return pipelineRepository.save(pipeline);
    }

    public Pipeline updatePipeline(Long id, PipelineRequest pipelineRequest) {
        Pipeline pipeline = findById(id);
        pipeline.setName(null);
        pipeline.setSort(pipelineRequest.getSort());
        return pipelineRepository.save(pipeline);
    }

    public void addStatus(Pipeline pipeline, Status status) {
        List<Status> statuses = pipeline.getStatuses();
        status = statusService.createStatus(status);
        statuses.add(status);
        pipelineRepository.save(pipeline);
    }

    public void deleteStatus(Long pipelineId, Long statusId) {
        Pipeline pipeline = findById(pipelineId);
        statusService.deleteStatus(statusId);
    }
}
