package uz.nexgroup.nexcrm.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import uz.nexgroup.nexcrm.request.LoginRequest;
import uz.nexgroup.nexcrm.request.RegistrationRequest;
import uz.nexgroup.nexcrm.response.TokenResponse;
import uz.nexgroup.nexcrm.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("${v1API}/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
	public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = 
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );
		return authService.generateTokenResponse(authentication);
	}

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("refresh_token") String refreshToken) {

		if (!authService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

		Authentication authentication = authService.getAuthenticationFromRefreshToken(refreshToken);

		TokenResponse tokenResponse = authService.generateTokenResponse(authentication);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest request) {
		authService.register(request.getEmail(), request.getName());
        return ResponseEntity.created(null).build();
    }
    
    @PostMapping("/reset")
    public String reset(@RequestBody String entity) {
        return entity;
    }
}
