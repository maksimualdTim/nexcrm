package uz.nexgroup.nexcrm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CompanyResponse {
    private Long id;

    private String name;

    @JsonProperty("responsible_id")
    private Long responsibleId;

    @JsonProperty("created_by")
    private Long createdById;

    @JsonProperty("updated_by")
    private Long updatedById;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    @JsonProperty("account_id")
    private Long accountId;
}
