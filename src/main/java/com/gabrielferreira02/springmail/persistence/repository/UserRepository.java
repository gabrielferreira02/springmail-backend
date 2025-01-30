package com.gabrielferreira02.springmail.persistence.repository;

import com.gabrielferreira02.springmail.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
