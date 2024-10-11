package uz.nexgroup.nexcrm.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LeadResponse {

    private Long id;

    private String name;

    private Long price;

    @JsonProperty("responsible_id")
    private Long responsibleId;

    @JsonProperty("status_id")
    private Long statusId;

    @JsonProperty("pipeline_id")
    private Long pipelineId;

    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("updated_by")
    private Long updatedBy;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    @JsonProperty("account_id")
    private Long accountId;
}
