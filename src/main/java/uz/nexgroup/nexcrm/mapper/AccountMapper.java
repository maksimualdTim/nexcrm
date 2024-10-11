package uz.nexgroup.nexcrm.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uz.nexgroup.nexcrm.model.Account;
import uz.nexgroup.nexcrm.model.Role;
import uz.nexgroup.nexcrm.response.AccountResponse;
import uz.nexgroup.nexcrm.response.RoleResponse;

@Component
public class AccountMapper {

    @Autowired
    RoleMapper roleMapper;

    public AccountResponse toResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setCountry(account.getCountry());
        accountResponse.setCurrency(account.getCurrency());
        accountResponse.setDomain(account.getDomain());

        List<Role> roles = account.getRoles();

        List<RoleResponse> roleResponses = new ArrayList<RoleResponse>();

        for (Role role : roles) {
            roleResponses.add(roleMapper.toResponse(role));
        }

        accountResponse.setRoles(roleResponses);

        return accountResponse;
    }

}
