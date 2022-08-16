package com.blog.blog.repository;

import com.blog.blog.domain.Post;
import com.blog.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public boolean existsByTitle(String title);
    public Optional<Post> findFirstPostByUser(User user);
    public void deletePostByUser(User user);
    public void deleteAllByUser(User user);

}
