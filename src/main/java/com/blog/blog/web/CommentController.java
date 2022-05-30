package com.blog.blog.web;

import com.blog.blog.BlogApplication;
import com.blog.blog.domain.Comment;
import com.blog.blog.repository.CommentRepository;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.service.CommentService;
import com.blog.blog.service.dto.CommentDto;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Path;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);

    public CommentController(CommentService commentService, CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments() {
        logger.debug("getting all comments");
        return new ResponseEntity<List<Comment>>(commentRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id){
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()){
            logger.debug("getting comment no: " + id);
            return new ResponseEntity<>(comment.get(), HttpStatus.OK);
        }else {
            logger.error("comment " + id + " not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND ,"comment " + id + " not found");
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody @Valid CommentDto dto) {
        if (!userRepository.existsById(dto.getUserId())
        || (!postRepository.existsById(dto.getPostId()))){
            logger.error("Error: not foud");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(commentService.postComment(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        if (commentRepository.existsById(id)){
            commentRepository.deleteById(id);
            logger.debug("comment " + id + " was deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> putComment(@RequestBody @Valid CommentDto dto, @PathVariable Long id) {
        if (!dto.noEmptyFields()) {
            logger.error("Error executing PUT request on comment " + id + " dto: " + dto);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!commentRepository.existsById(id)
        || (!userRepository.existsById(dto.getUserId()))
        || (!postRepository.existsById(dto.getPostId()))) {
            logger.error("Error: not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(commentService.putComment(dto, id).get(), HttpStatus.OK);
    }

    @PatchMapping("/comments/{id}")
    public  ResponseEntity<Comment> patchComment(@RequestBody @Valid CommentDto dto, @PathVariable Long id) {
        if (!commentRepository.existsById(id)
        || (dto.getPostId() != null && !postRepository.existsById(dto.getPostId()))
        || (dto.getUserId() != null && !userRepository.existsById(dto.getUserId()))){
            logger.error("error: not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(commentService.update(dto, id).get(), HttpStatus.OK);
    }

    @GetMapping("/comments/csv")
    public ResponseEntity downloadCsv() throws IOException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comments.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(new ByteArrayInputStream(commentService.writeCommentsToCsv())));
    }


    @PostMapping(path = "/comments/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String ingestCsv(@RequestPart(required = true) MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, CsvValidationException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "No File is Present");
            return "redirect:uploadStatus";
        } else {
            commentService.importCsv(file);
            return "file was uploaded successfully";
        }
    }

    @GetMapping("/comments/xml")
    public ResponseEntity downloadXml() throws JAXBException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comments.xml")
                .contentType(MediaType.parseMediaType("application/xml"))
                .body(new InputStreamResource(commentService.toXml()));
    }

    @PostMapping(path = "/comments/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String ingestXml(@RequestPart(required = true) MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, CsvValidationException, JAXBException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "No File is Present");
            return "redirect:uploadStatus";
        } else {
            commentService.importXml(file);
            return "file was uploaded successfully";
        }
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping("/comments/pdf")
    public ResponseEntity downloadPdf(){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comments.pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(new ByteArrayInputStream(commentService.exportPdf())));
    }
}
