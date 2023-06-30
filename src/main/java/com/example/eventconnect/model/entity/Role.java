package com.example.eventconnect.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
public class Role {
    @Id
    @Enumerated(value = EnumType.STRING)
    private RoleEnum name;
}
