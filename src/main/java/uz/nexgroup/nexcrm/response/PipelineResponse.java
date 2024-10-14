package uz.nexgroup.nexcrm.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PipelineResponse {
    private Long id;

    private String name;

    private int sort;

    @JsonProperty("is_main")
    private boolean isMain;

    private List<StatusResponse> statuses;

    @JsonProperty("account_id")
    private Long accountId;
    
}
