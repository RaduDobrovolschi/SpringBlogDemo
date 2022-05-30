package com.blog.blog.web;

import com.blog.blog.domain.Comment;
import com.blog.blog.domain.Post;
import com.blog.blog.service.ClientPostService;
import com.blog.blog.service.dto.CommentDto;
import com.blog.blog.service.dto.PostDto;
import org.apache.catalina.LifecycleState;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/external")
public class ClientPostController {
    private final ClientPostService clientPostService;

    public ClientPostController(ClientPostService clientPostService) {
        this.clientPostService = clientPostService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        return new ResponseEntity<>(clientPostService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id){
        return new ResponseEntity<>(clientPostService.getPost(id), HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> postPost(@RequestBody PostDto dto) throws URISyntaxException {
        return new ResponseEntity<>(clientPostService.postPost(dto), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        clientPostService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> putPost(@RequestBody PostDto dto,@PathVariable Long id){
        return new ResponseEntity<>(clientPostService.putPost(dto, id), HttpStatus.OK);
    }
}
