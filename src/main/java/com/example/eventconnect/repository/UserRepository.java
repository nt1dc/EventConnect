package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}