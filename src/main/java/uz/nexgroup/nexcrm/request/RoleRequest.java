package uz.nexgroup.nexcrm.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import uz.nexgroup.nexcrm.model.Permission;

@Data
public class RoleRequest {
    @NotBlank
    @Length(min = 3, max = 255)
    private String name;

    private Permission permissions;
}
