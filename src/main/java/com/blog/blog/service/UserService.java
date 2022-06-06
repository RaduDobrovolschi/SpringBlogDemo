package com.blog.blog.service;

import com.blog.blog.BlogApplication;
import com.blog.blog.domain.User;
import com.blog.blog.repository.*;
import com.blog.blog.service.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserRoleRepository userRoleRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, UserStatusRepository userStatusRepository, UserRoleRepository userRoleRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.userRoleRepository = userRoleRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public ResponseEntity<List<User>> findAll(){
        logger.debug("getting all users");
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    public User register(UserDto dto){
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setStatus(userStatusRepository.getUserStatusByStatus("Active"));
        user.addRole(userRoleRepository.getUserRoleByRole("user"));
        userRepository.save(user);
        logger.debug("Created new user: " + user);
        return user;
    }

    public Optional<User> updateUserInfo(UserDto dto, Long id){
        return Optional
                .of(userRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    if (dto.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    }
                    if (dto.getUsername() != null) user.setUsername(dto.getUsername());
                    if (dto.getEmail() != null) user.setEmail(dto.getEmail());
                    if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
                    if (dto.getLastName() != null) user.setLastName(dto.getLastName());
                    if (dto.getRole() != null) {
                        user.addRole(userRoleRepository.findById(dto.getRole()).get());
                    }
                    return user;
                });
    }

    public Optional<User> putUser(UserDto dto, Long id){
        return Optional
                .of(userRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.addRole(userRoleRepository.findUserRoleByRole(dto.getRole()).get());
                    user.setUsername(dto.getUsername());
                    user.setLastName(dto.getLastName());
                    user.setFirstName(dto.getFirstName());
                    user.setEmail(dto.getEmail());
                    user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    return user;
                });
        }

    public void delete(Long id){
        logger.debug("deleting user id: " + id);
        User user = userRepository.findById(id).get();
        postRepository.deleteAllByUser(user);
        commentRepository.deleteAllByUser(user);
        userRepository.deleteById(id);
    }
}
