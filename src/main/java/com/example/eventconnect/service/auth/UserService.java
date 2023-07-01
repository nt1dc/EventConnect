package com.example.eventconnect.service.auth;

import com.example.eventconnect.model.entity.user.User;

public interface UserService {

    User getUserByLogin(String username);
}
