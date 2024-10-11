package uz.nexgroup.nexcrm.response;

import lombok.Data;
import uz.nexgroup.nexcrm.model.Permission;

@Data
public class RoleResponse {

    private Long id;

    private String name;
    
    private Permission permissions;
}
