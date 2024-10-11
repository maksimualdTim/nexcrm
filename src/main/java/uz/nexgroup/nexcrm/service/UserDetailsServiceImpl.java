package uz.nexgroup.nexcrm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.repository.RefreshTokenRepository;
import uz.nexgroup.nexcrm.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        // Check if the user is present and handle accordingly
        User user = optionalUser.orElseThrow(() -> {
            return new UsernameNotFoundException("User not found in the database");
        });

        Collection <SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new uz.nexgroup.nexcrm.model.UserDetailsImpl(
            user.getId(), 
            user.getEmail(), 
            user.getName(), 
            user.getPassword(), 
            user.getAccount(), 
            user.getRole()
        );
    }
}
