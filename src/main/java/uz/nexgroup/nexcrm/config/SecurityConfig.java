/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package uz.nexgroup.nexcrm.config;

 import java.security.interfaces.RSAPrivateKey;
 import java.security.interfaces.RSAPublicKey;
 
 import com.nimbusds.jose.jwk.JWK;
 import com.nimbusds.jose.jwk.JWKSet;
 import com.nimbusds.jose.jwk.RSAKey;
 import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
 import com.nimbusds.jose.jwk.source.JWKSource;
 import com.nimbusds.jose.proc.SecurityContext;

import uz.nexgroup.nexcrm.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
 import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
 import org.springframework.security.oauth2.jwt.JwtEncoder;
 import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
 import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
 import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 import java.util.Arrays;
 /**
  * Security configuration for the main application.
  *
  * @author Josh Cummings
  */
 @Configuration
 @EnableAsync
 @EnableWebSecurity
 public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
 
     @Value("${jwt.public.key}")
     RSAPublicKey key;
 
     @Value("${jwt.private.key}")
     RSAPrivateKey priv;

     @Value("${v1API}")
     private String api;
 
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         // @formatter:off
         http
                .cors((cors)->{
                    cors.configurationSource(apiConfigurationSource());
                })
                 .authorizeHttpRequests((authorize) -> authorize
                         .requestMatchers(api + "/auth/**").permitAll()
                         .requestMatchers(api + "/account/**").hasAnyRole("ADMIN")
                         .anyRequest().authenticated()
                 )
                 .csrf((csrf) -> {
                    csrf.ignoringRequestMatchers(api + "/**");
                 })
                 .httpBasic(Customizer.withDefaults())
                 .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                 .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .exceptionHandling((exceptions) -> exceptions
                         .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                         .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                 );
         // @formatter:on
         return http.build();
     }
 	CorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
     @Bean
     JwtDecoder jwtDecoder() {
         return NimbusJwtDecoder.withPublicKey(this.key).build();
     }
 
     @Bean
     JwtEncoder jwtEncoder() {
         JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
         JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
         return new NimbusJwtEncoder(jwks);
     }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

	@Bean
	public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}
 }
