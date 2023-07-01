package com.example.eventconnect.service.auth;

import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByLogin(String username) {
        return userRepository.findByLogin(username).orElseThrow(RuntimeException::new);
    }
}
