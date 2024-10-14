package uz.nexgroup.nexcrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import uz.nexgroup.nexcrm.mapper.AccountMapper;
import uz.nexgroup.nexcrm.mapper.RoleMapper;
import uz.nexgroup.nexcrm.model.Account;
import uz.nexgroup.nexcrm.model.Role;
import uz.nexgroup.nexcrm.model.UserDetailsImpl;
import uz.nexgroup.nexcrm.request.RoleRequest;
import uz.nexgroup.nexcrm.response.AccountResponse;
import uz.nexgroup.nexcrm.response.RoleResponse;
import uz.nexgroup.nexcrm.service.RoleService;
import uz.nexgroup.nexcrm.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("${v1API}/account")
public class AccountController {
    @Autowired
    AccountMapper accountMapper;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMapper roleMapper;

    @GetMapping
    public AccountResponse accountSettings() {
        UserDetailsImpl userDetailsImpl = userService.getCurrentUser();
        Account account = userDetailsImpl.getAccount();

        return accountMapper.toResponse(account);
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        Role role = roleMapper.toModel(roleRequest);
        role = roleService.creatRole(role);
        RoleResponse roleResponse = roleMapper.toResponse(role);
        return ResponseEntity.created(null).body(roleResponse);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody RoleRequest roleRequest, @PathVariable Long id) {
        Role role = roleMapper.toModel(roleRequest);
        role = roleService.updateRole(id, role);
        RoleResponse roleResponse = roleMapper.toResponse(role);
        return ResponseEntity.ok().body(roleResponse);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
    
}
