package com.example.eventconnect.service.auth;

import com.example.eventconnect.model.entity.User;

public interface UserService {

    User getUserByLogin(String username);
}
