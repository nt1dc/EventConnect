package com.example.eventconnect.model.dto.auth;

import com.example.eventconnect.model.entity.user.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Email
    private String login;
    @NotEmpty
    @Size(min = 6, max = 30)
    private String password;
    @NotNull
    private RoleEnum role;
}
