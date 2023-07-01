package com.example.eventconnect.model.dto.auth;

import com.example.eventconnect.model.entity.user.RoleEnum;
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
