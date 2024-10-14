package uz.nexgroup.nexcrm.request;

import java.util.List;

import lombok.Data;

@Data
public class PipelineRequest {
    private String name;
    private int sort;

    private List<StatusRequest> statuses;
}
