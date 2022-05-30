package com.blog.blog.repository;

import com.blog.blog.domain.PostStatus;
import com.blog.blog.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostStatusRepository extends JpaRepository<PostStatus, Long> {
    public PostStatus getPostStatusByStatus(String status);
    public Optional<PostStatus> findPostStatusByStatus(String status);
    public boolean existsByStatus(String status);
}
