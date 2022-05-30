package com.blog.blog.web;

import com.blog.blog.BlogApplication;
import com.blog.blog.domain.User;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.repository.UserRoleRepository;
import com.blog.blog.service.UserService;
import com.blog.blog.service.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;


    public UserController(UserService userService, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserDto dto){
        if (dto.getUsername() == null
        || (dto.getEmail() == null)
        || (dto.getPassword() == null)
        || (userRepository.existsByEmail(dto.getEmail())
        || (userRepository.existsByUsername(dto.getUsername())))){
            logger.error("Invalid input data {}" + dto.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.register(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto dto, @PathVariable Long id){
        if (!userRepository.existsById(id)
        || (dto.getRole() != null && !userRoleRepository.existsByRole(dto.getRole()))){
            logger.error("not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userService.updateUserInfo(dto, id).get(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        logger.debug("getting post no " + id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            logger.debug("returning user " + user.get().toString());
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            logger.debug("user " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> putUser(@RequestBody UserDto dto, @PathVariable Long id){
        if (!dto.noEmptyFields()){
            logger.error("dto shouldnt have empty fields");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!userRepository.existsById(id)
        || (!userRoleRepository.existsByRole(dto.getRole()))){
            logger.error("not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.putUser(dto, id).get(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        if (!userRepository.existsById(id)){
            logger.error("user " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
