package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.user.Role;
import com.example.eventconnect.model.entity.user.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, RoleEnum> {
    Optional<Role> findRoleByName(RoleEnum roleEnum);
}