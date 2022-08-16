package com.blog.blog.repository;

import com.blog.blog.domain.Comment;
import com.blog.blog.domain.Post;
import com.blog.blog.domain.User;
import liquibase.pro.packaged.C;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Optional<Comment> findFirstCommentByUser(User user);
    public void deleteCommentByUser(User user);
    public void deleteAllByPost(Post post);
    public void deleteAllByUser(User user);
    public boolean existsById(Long id);
    public void deleteAll();
    public void deleteAllById(Long id);
    public List<Comment> findAllByPost(Post post);
    public Long countByPost(Post post);
    public boolean existsByPost(Post post);

}
