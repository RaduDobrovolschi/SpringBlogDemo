package com.blog.blog.web;

import com.blog.blog.domain.User;
import com.blog.blog.service.ClientUserService;
import com.blog.blog.service.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/external")
public class ClientUserController {
    ClientUserService clientUserService;

    public ClientUserController(ClientUserService clientUserService){
        this.clientUserService = clientUserService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllPosts(){
        return new ResponseEntity<>(clientUserService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        return new ResponseEntity<>(clientUserService.getUser(id), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> postUser(@RequestBody UserDto dto){
        return new ResponseEntity<>(clientUserService.postUser(dto), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        clientUserService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> putUser(@PathVariable Long id, @RequestBody UserDto dto){
        return new ResponseEntity<>(clientUserService.putUser(dto, id), HttpStatus.OK);
    }
}
