package com.blog.blog.repository;

import com.blog.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PutMapping;

import java.sql.Struct;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}
