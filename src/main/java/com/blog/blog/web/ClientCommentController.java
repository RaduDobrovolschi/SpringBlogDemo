package com.blog.blog.web;

import com.blog.blog.domain.Comment;
import com.blog.blog.service.ClientCommentService;
import com.blog.blog.service.dto.CommentDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/external")
public class ClientCommentController {
    private final ClientCommentService clientCommentService;

    public ClientCommentController(ClientCommentService clientCommentService) {
        this.clientCommentService = clientCommentService;
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments(){
        return new ResponseEntity<>(clientCommentService.getAllComments(), HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id){
        return ResponseEntity.ok().headers(new HttpHeaders()).body(clientCommentService.getComment(id));
        //new ResponseEntity<>(clientCommentService.getComment(id), HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> postComment(@RequestBody CommentDto dto) throws URISyntaxException {
        return new ResponseEntity<>(clientCommentService.postComment(dto), HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> putComment(@RequestBody CommentDto dto, @PathVariable Long id){
        return new ResponseEntity<>(clientCommentService.putComment(dto, id), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delteComment(@PathVariable Long id){
        clientCommentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
