package uz.nexgroup.nexcrm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import uz.nexgroup.nexcrm.exception.DuplicateEmailException;
import uz.nexgroup.nexcrm.model.Role;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.model.UserDetailsImpl;
import uz.nexgroup.nexcrm.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public User createUser(String email, String name, String password) throws DuplicateEmailException{
        try {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(passwordEncoder.encode(password));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException("User with email " + email + " already exists");
        }
    }

    public void setRole(User user, Role role) {
        user.setRole(role);
        userRepository.save(user);
    }

    public UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
        if(authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getSubject();
            Optional<User> userOptional = userRepository.findByEmail(email);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                return new UserDetailsImpl(user.getId(), user.getEmail(), user.getName(), user.getPassword(), user.getAccount(), user.getRole());
            }
        }
        return null;
    }

    public void setPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }
}
