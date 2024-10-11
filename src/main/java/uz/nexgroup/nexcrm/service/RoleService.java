package uz.nexgroup.nexcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import uz.nexgroup.nexcrm.exception.AdmiRoleDeleteException;
import uz.nexgroup.nexcrm.exception.DuplicateRoleException;
import uz.nexgroup.nexcrm.model.Account;
import uz.nexgroup.nexcrm.model.Permission;
import uz.nexgroup.nexcrm.model.Role;
import uz.nexgroup.nexcrm.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role creatRole(String name, Account account, Permission permissions) throws DuplicateRoleException{
        Role role = generateRole(name, account, permissions);
        role.setAdmin(false);
        try {
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRoleException("Role " + name + " already exists");
        }
    }

    public Role creatRole(String name, Account account, Permission permissions, boolean is_admin) throws DuplicateRoleException{
        Role role = generateRole(name, account, permissions);
        role.setAdmin(is_admin);
        try {
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRoleException("Role " + name + " already exists");
        }
    }

    public Role creatRole(Role role) throws DuplicateRoleException{
        try {
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRoleException("Role " + role.getName() + " already exists");
        }
    }

    public Role updateRole(Long id, Role roleBody) {
        Role role = roleRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Role with id: " + id + " not found");
        });

        role.setAccount(roleBody.getAccount());
        role.setAdmin(false);
        role.setName(roleBody.getName());
        role.setPermissions(roleBody.getPermissions());
        return roleRepository.save(role);
    }

    private Role generateRole(String name, Account account, Permission permissions) {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(permissions);
        role.setAccount(account);
        return role;
    }

    public void deleteRole(Long id) throws AdmiRoleDeleteException, EntityNotFoundException{
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        if(role.isAdmin()) {
            throw new AdmiRoleDeleteException("You could not delete admin role");
        }
        roleRepository.deleteById(id);
    }
}
