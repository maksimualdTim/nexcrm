package uz.nexgroup.nexcrm.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StatusResponse {
    private Long id;

    private String name;

    private int sort;

    @JsonProperty("pipline_id")
    private Long pipelineId;

    @JsonProperty("account_id")
    private Long accountId;
}
