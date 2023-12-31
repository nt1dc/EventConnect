package com.example.eventconnect.service.auth;

import com.example.eventconnect.exception.UserAlreadyExistsException;
import com.example.eventconnect.model.dto.auth.AuthRequest;
import com.example.eventconnect.model.dto.auth.AuthResponse;
import com.example.eventconnect.model.dto.auth.RegisterRequest;
import com.example.eventconnect.model.entity.user.Role;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.repository.RoleRepository;
import com.example.eventconnect.repository.UserRepository;
import com.example.eventconnect.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(UserService userService, JwtTokenProvider jwtTokenProvider,
                           UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByLogin(registerRequest.getLogin()).isPresent())
            throw new UserAlreadyExistsException("user with login " + registerRequest.getLogin() + " already exist");
        Role role = roleRepository.findRoleByName(registerRequest.getRole()).orElseThrow();
        userRepository.save(
                User.builder().login(registerRequest.getLogin())
                        .roles(Set.of(role))
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .build()
        );
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        User user = userService.getUserByLogin(authRequest.getLogin());
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
            throw new BadCredentialsException("invalid login or password");
        String accessToken = jwtTokenProvider.createAccessToken(authRequest.getLogin(), authRequest.getPassword());
        String refreshToken = jwtTokenProvider.createRefreshToken(authRequest.getLogin(), authRequest.getPassword());
        return new AuthResponse(
                user.getLogin(),
                accessToken,
                refreshToken,
                user.getRoles()
        );
    }

    @Override
    public String generateRefreshTokenByAccess(String token) {
        return jwtTokenProvider.createAccessTokenByRefreshToken(token);
    }
}
