package com.blog.blog.repository;

import com.blog.blog.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    public UserStatus getUserStatusByStatus(String status);
}
