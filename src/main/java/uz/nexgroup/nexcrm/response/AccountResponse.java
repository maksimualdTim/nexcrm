package uz.nexgroup.nexcrm.response;

import java.util.List;

import lombok.Data;

@Data
public class AccountResponse {
    private String domain;

    private String country;

    private String currency;

    private List<RoleResponse> roles;
}
