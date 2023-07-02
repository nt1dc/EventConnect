package com.example.eventconnect.security;

import com.example.eventconnect.model.entity.user.RoleEnum;
import com.example.eventconnect.service.auth.UserService;
import com.example.eventconnect.service.auth.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtTokenFilter jwtTokenFilter;

    private final UserServiceImpl detailService;
    private final AuthenticationEntryPoint authEntryPoint;

    private static final String AUTH_ENDPOINT = "/auth/**";
    private static final String ADMIN_ENDPOINT = "/admin/**";
    private static final String PARTICIPANT_ENDPOINT = "/participant/**";
    private static final String EVENT_ADMIN_ENDPOINT = "/event-admin/**";
    private static final String EVENT_INFO_ENDPOINT = "/event";


    public SecurityConfig(JwtTokenFilter jwtTokenFilter, UserServiceImpl detailService
            , @Qualifier("delegatedAuthenticationEntryPoint") AuthenticationEntryPoint authEntryPoint
    ) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.detailService = detailService;
        this.authEntryPoint = authEntryPoint;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(EVENT_INFO_ENDPOINT).permitAll()
                        .requestMatchers(AUTH_ENDPOINT).permitAll()
                        .requestMatchers(PARTICIPANT_ENDPOINT).hasAuthority(RoleEnum.PARTICIPANT.name())
                        .requestMatchers(ADMIN_ENDPOINT).hasAuthority(RoleEnum.ADMIN.name())
                        .requestMatchers(EVENT_ADMIN_ENDPOINT).hasAuthority(RoleEnum.EVENT_ADMIN.name())
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(authEntryPoint))
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
        authProvider.setUserDetailsService(detailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
