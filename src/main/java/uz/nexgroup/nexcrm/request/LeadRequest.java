package uz.nexgroup.nexcrm.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeadRequest {
    private String name;

    private Long price;

    @JsonProperty("status_id")
    private Long statusId;

    @JsonProperty("created_by")
    private Long createdById;

    @JsonProperty("updated_by")
    private Long updatedById;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("responsible_id")
    private Long responsibleId;

}
