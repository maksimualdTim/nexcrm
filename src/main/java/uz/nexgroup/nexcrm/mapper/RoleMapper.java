package uz.nexgroup.nexcrm.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uz.nexgroup.nexcrm.model.Role;
import uz.nexgroup.nexcrm.request.RoleRequest;
import uz.nexgroup.nexcrm.response.RoleResponse;
import uz.nexgroup.nexcrm.service.UserService;

@Component
public class RoleMapper {

    @Autowired
    private UserService userService;

    public RoleResponse toResponse(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setName(role.getName());
        roleResponse.setPermissions(role.getPermissions());
        return roleResponse;
    }

    public Role toModel(RoleResponse roleResponse) {
        Role role = new Role();
        role.setAdmin(false);
        role.setAccount(userService.getCurrentUser().getAccount());
        role.setName(roleResponse.getName());
        role.setPermissions(roleResponse.getPermissions());

        return role;
    }

    public Role toModel(RoleRequest roleRequest) {
        Role role = new Role();
        role.setAccount(userService.getCurrentUser().getAccount());
        role.setAdmin(false);
        role.setName(roleRequest.getName());
        role.setPermissions(roleRequest.getPermissions());

        return role;
    }
}
