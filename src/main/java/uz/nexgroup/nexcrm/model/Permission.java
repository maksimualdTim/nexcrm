package uz.nexgroup.nexcrm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Permission {

    private PermissionDetail leads;
    private PermissionDetail contacts;
    private PermissionDetail companies;
    private PermissionDetail tasks;

    @Data
    public static class PermissionDetail {
        private String view;
        private String edit;
        private String add;
        private String delete;
        private String export;
    }
}