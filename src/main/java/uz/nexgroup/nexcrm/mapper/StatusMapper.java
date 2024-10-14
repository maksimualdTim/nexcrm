package uz.nexgroup.nexcrm.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uz.nexgroup.nexcrm.model.Status;
import uz.nexgroup.nexcrm.request.StatusRequest;
import uz.nexgroup.nexcrm.response.StatusResponse;
import uz.nexgroup.nexcrm.service.UserService;

@Component
public class StatusMapper {
    @Autowired
    UserService userService;

    public StatusResponse toResponse(Status status) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setAccountId(userService.getCurrentUser().getAccount().getId());
        statusResponse.setName(status.getName());
        statusResponse.setId(status.getId());
        statusResponse.setPipelineId(status.getPipeline().getId());
        statusResponse.setSort(status.getSort());
        return statusResponse;
    }

    public Status toModel(StatusRequest statusRequest) {
        Status status = new Status();
        status.setName(statusRequest.getName());
        status.setSort(statusRequest.getSort());
        return status;
    }

    public List<Status> toModel(List<StatusRequest> statusRequests) {
        return statusRequests.stream().map(this::toModel).toList();
    }
}
