package uz.nexgroup.nexcrm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import uz.nexgroup.nexcrm.exception.DuplicateDomainException;
import uz.nexgroup.nexcrm.model.Account;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(String domain) throws DuplicateDomainException{
        try {
            Account account = new Account();

            account.setCountry("ru");
            account.setCurrency("RUB");
            account.setDomain(domain);
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDomainException("Account with domain + " + domain + " already exists");
        }
    }

    public void addUserToAccount(Account account, User user) {
        user.setAccount(account);
        if(account.getUsers() != null) {
            account.getUsers().add(user);
        } else {
            List<User> users = new ArrayList<User>();
            users.add(user);
            account.setUsers(users);
        }
        
        accountRepository.save(account);
    }
}
