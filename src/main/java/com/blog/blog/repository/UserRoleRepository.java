package com.blog.blog.repository;

import com.blog.blog.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    public UserRole getUserRoleByRole(String role);
    public Optional<UserRole> findUserRoleByRole(String role);
    public boolean existsByRole (String role);
}
