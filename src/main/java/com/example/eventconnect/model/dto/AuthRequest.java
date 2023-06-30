package com.example.eventconnect.model.dto;

import com.example.eventconnect.model.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class AuthRequest {
    private String login;
    private String password;

}
