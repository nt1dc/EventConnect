package com.example.eventconnect.security;

import com.example.eventconnect.model.entity.Role;
import com.example.eventconnect.model.entity.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtTokenFilter jwtTokenFilter;

    private final JwtUserDetailsService jwtUserDetailsService;


    private static final String AUTH_ENDPOINT = "/auth/**";
    private static final String ADMIN_ENDPOINT = "/admin/**";
    private static final String PARTICIPANT_ENDPOINT = "/participant/**";
    private static final String EVENT_ADMIN_ENDPOINT = "/event-admin/**";


    public SecurityConfig(JwtTokenFilter jwtTokenFilter, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(AUTH_ENDPOINT).permitAll()
                        .requestMatchers(PARTICIPANT_ENDPOINT).hasAuthority(RoleEnum.PARTICIPANT.name())
                        .requestMatchers(ADMIN_ENDPOINT).hasAuthority(RoleEnum.ADMIN.name())
                        .requestMatchers(EVENT_ADMIN_ENDPOINT).hasAuthority(RoleEnum.EVENT_ADMIN.name())
                        .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
