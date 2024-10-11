package uz.nexgroup.nexcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import uz.nexgroup.nexcrm.component.DomainUtils;
import uz.nexgroup.nexcrm.component.PasswordGenerator;
import uz.nexgroup.nexcrm.model.Account;
import uz.nexgroup.nexcrm.model.Permission;
import uz.nexgroup.nexcrm.model.RefreshToken;
import uz.nexgroup.nexcrm.model.Role;
import uz.nexgroup.nexcrm.model.User;
import uz.nexgroup.nexcrm.repository.RefreshTokenRepository;
import uz.nexgroup.nexcrm.repository.UserRepository;
import uz.nexgroup.nexcrm.response.TokenResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Service
public class AuthService {
    @Autowired
	JwtEncoder encoder;

    @Autowired
    JwtDecoder decoder;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRepository userRepository;

    public TokenResponse generateTokenResponse(Authentication authentication) {
        String accessToken = generateToken(authentication,36000L);
        String refreshToken = generateToken(authentication, 36000L * 24 * 30 * 3);
        Optional<User> userOptional = userRepository.findByEmail(authentication.getName());
        User user = null;

        if(userOptional.isPresent()) {
            user = userOptional.get();
        }

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUser(user);
        if (!refreshTokenOptional.isPresent() || isTokenExpiringSoon(refreshTokenOptional.get())) {
            RefreshToken refreshTokenModel = new RefreshToken();
            refreshTokenModel.setToken(refreshToken);
            refreshTokenModel.setCreatedAt(LocalDateTime.now());
            refreshTokenModel.setExpiresAt(LocalDateTime.now().plus(3, ChronoUnit.MONTHS));
            refreshTokenModel.setUser(user);
            refreshTokenModel = refreshTokenRepository.save(refreshTokenModel);
        }

        TokenResponse token = new TokenResponse();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpiresIn(36000);
        return token;
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        
        if (refreshToken.isPresent()) {
            // Проверка срока действия
            return !refreshToken.get().getExpiresAt().isBefore(LocalDateTime.now());
        }
        return false;
    }

    public Authentication getAuthenticationFromRefreshToken(String refreshToken) {
        UserDetails userDetails = loadUserDetailsByRefreshToken(refreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public void register(String email, String name) {
        Account account = accountService.createAccount(name);
        String domain = DomainUtils.generateDomain(name);
        account.setDomain(domain);

        String password = PasswordGenerator.generateRandomPassword();
        User user = userService.createUser(email, name, password);

        Permission.PermissionDetail permissionDetail = new Permission.PermissionDetail();
        permissionDetail.setAdd("A");
        permissionDetail.setDelete("A");
        permissionDetail.setEdit("A");
        permissionDetail.setExport("A");
        permissionDetail.setView("A");

        Permission permissions = new Permission();
        permissions.setLeads(permissionDetail);
        permissions.setCompanies(permissionDetail);
        permissions.setContacts(permissionDetail);
        permissions.setTasks(permissionDetail);

        Role role = roleService.creatRole("ADMIN", account, permissions, true);
        userService.setRole(user, role);

        accountService.addUserToAccount(account, user);
        emailService.sendEmail(email, "Regstration", "Your login: " + email + " your password: " + password);
    }

    private UserDetails loadUserDetailsByRefreshToken(String refreshToken) {
        Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken);
        
        if (token.isPresent()) {
            User user = token.get().getUser();
            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(authority)
            );
        }
        
        throw new UsernameNotFoundException("User not found for refresh token");
    }

    private String generateToken(Authentication authentication, long expiry) {
		Instant now = Instant.now();
		// @formatter:off
		String role = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getName())
				.claim("role", role)
				.build();
		// @formatter:on
		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private boolean isTokenExpiringSoon(RefreshToken refreshToken) {
        LocalDateTime oneDayFromNow = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        return refreshToken.getExpiresAt().isBefore(oneDayFromNow);
    }
}
