package com.bank.movement.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity; 
import org.springframework.security.config.web.server.ServerHttpSecurity; 
import org.springframework.security.core.userdetails.ReactiveUserDetailsService; 
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain; 

import reactor.core.publisher.Mono; 

@Configuration
@EnableWebFluxSecurity 
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) 
                .authorizeExchange(exchanges -> exchanges 
                        .pathMatchers("/api/v1/reports/**").permitAll() 
                        .anyExchange().authenticated() 
                )
                .httpBasic(org.springframework.security.config.Customizer.withDefaults()) 
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("password")) // Encode the password
                .roles("USER")
                .build();
        return username -> Mono.justOrEmpty(username.equals(user.getUsername()) ? user : null);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Good practice to use a password encoder
    }
}