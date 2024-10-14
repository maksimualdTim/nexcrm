package uz.nexgroup.nexcrm.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uz.nexgroup.nexcrm.model.Pipeline;
import uz.nexgroup.nexcrm.request.PipelineRequest;
import uz.nexgroup.nexcrm.response.PipelineResponse;
import uz.nexgroup.nexcrm.response.StatusResponse;
import uz.nexgroup.nexcrm.service.UserService;

@Component
public class PipelineMapper {
    @Autowired
    UserService userService;

    @Autowired
    StatusMapper statusMapper;
    
    public PipelineResponse toResponse(Pipeline pipeline) {
        PipelineResponse pipelineResponse = new PipelineResponse();

        pipelineResponse.setId(pipeline.getId());
        pipelineResponse.setMain(pipeline.getMain());
        pipelineResponse.setName(pipeline.getName());
        pipelineResponse.setSort(pipeline.getSort());
        List<StatusResponse> statuses = pipeline.getStatuses().stream().map(statusMapper::toResponse).collect(Collectors.toList());
        pipelineResponse.setStatuses(statuses);
        pipelineResponse.setAccountId(userService.getCurrentUser().getAccount().getId());
        return pipelineResponse;
    }

    public List<PipelineResponse> toResponse(List<Pipeline> pipelines) {
        return pipelines.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Pipeline toModel(PipelineRequest pipelineRequest) {
        Pipeline pipeline = new Pipeline();

        pipeline.setAccount(userService.getCurrentUser().getAccount());
        pipeline.setName(pipelineRequest.getName());
        pipeline.setMain(false);
        pipeline.setSort(pipelineRequest.getSort());
        pipeline.setStatuses(statusMapper.toModel(pipelineRequest.getStatuses()));
        return pipeline;
    }
}
