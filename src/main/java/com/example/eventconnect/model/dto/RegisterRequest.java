package com.example.eventconnect.model.dto;

import com.example.eventconnect.model.entity.Role;
import com.example.eventconnect.model.entity.RoleEnum;
import lombok.*;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String login;
    private String password;
    private RoleEnum role;
}
