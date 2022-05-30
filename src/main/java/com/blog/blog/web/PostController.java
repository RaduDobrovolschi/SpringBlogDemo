package com.blog.blog.web;

import com.blog.blog.BlogApplication;
import com.blog.blog.domain.Post;
import com.blog.blog.domain.PostStatus;
import com.blog.blog.domain.User;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.PostStatusRepository;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.service.PostService;
import com.blog.blog.service.dto.PostDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);
    private final UserRepository userRepository;
    private final PostStatusRepository postStatusRepository;


    public PostController(PostRepository postRepository, PostService postService, UserRepository userRepository, PostStatusRepository postStatusRepository){ this.postRepository = postRepository;
        this.postService = postService;
        this.userRepository = userRepository;
        this.postStatusRepository = postStatusRepository;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        logger.debug("getting all posts");
        return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id){
        logger.debug("getting post no " + id + ":");
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()){
            logger.debug("retunring post no " + id + ":");
            return  new ResponseEntity<>(post.get(), HttpStatus.OK);
        }else {
            logger.error("Post " + id + " not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found");
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost( @RequestBody PostDto dto) {
        if (!userRepository.existsById(dto.getUserId())
        || (!postStatusRepository.existsByStatus(dto.getStatus()))){
            logger.error("error: not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (postRepository.existsByTitle(dto.getTitle())){
            logger.error("Title already exists " + dto.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(postService.savePost(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        logger.debug("deleting post id: " + id);
        if (postRepository.findById(id).isPresent()) {
            postService.deletePost(id);
            logger.debug("post " + id + " was deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            logger.error("psot " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> putPost(@RequestBody PostDto dto ,@PathVariable Long id){
        if (!dto.noEmptyFields()
        || (postRepository.existsByTitle(dto.getTitle()))){
            logger.error("post dto has empty fields " + dto.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!userRepository.existsById(dto.getUserId())
        ||(!postStatusRepository.existsByStatus(dto.getStatus()))
        ||(!postRepository.existsById(id))){
            logger.error("error: not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postService.putPost(dto, id).get(), HttpStatus.OK);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<Post> patchPost(@RequestBody PostDto dto, @PathVariable Long id) {
        if (!postRepository.existsById(id)
        || (dto.getUserId() != null && !userRepository.existsById(dto.getUserId()))
        || (dto.getStatus() != null && !postStatusRepository.existsByStatus(dto.getStatus()))){
            logger.error("error: not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (postRepository.existsByTitle(dto.getTitle())){
            logger.error("Title already exists " + dto.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.updatePost(dto, id).get(), HttpStatus.OK);
    }

    @GetMapping("/posts/pdf")
    public ResponseEntity downloadPdf(){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comments.pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(new ByteArrayInputStream(postService.exportPdf())));
    }
}
