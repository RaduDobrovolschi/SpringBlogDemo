package com.blog.blog.service;

import com.blog.blog.BlogApplication;
import com.blog.blog.domain.Comment;
import com.blog.blog.domain.Post;
import com.blog.blog.repository.CommentRepository;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.PostStatusRepository;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.service.dto.PostDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostStatusRepository postStatusRepository;
    private final CommentRepository commentRepository;
    private final MinioService minioService;
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);


    public PostService(UserRepository userRepository, PostRepository postRepository, PostStatusRepository postStatusRepository, CommentRepository commentRepositorys, MinioService minioService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postStatusRepository = postStatusRepository;
        this.commentRepository = commentRepositorys;
        this.minioService = minioService;
    }

    public Post savePost(PostDto dto){
        logger.debug("creating new post " + dto.toString());
        Post post = new Post();
        post.setUser(userRepository.findById(dto.getUserId()).get());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setStatus(postStatusRepository.findPostStatusByStatus(dto.getStatus()).get());
        post.setCreateTime(Instant.now());
        postRepository.save(post);
        logger.debug("Post service generated post: " + post);
        return post;
    }

    public Optional<Post> updatePost(PostDto dto, Long id){
        return Optional
                .of(postRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(post -> {
                    if (dto.getUserId() != null) post.setUser(userRepository.findById(dto.getUserId()).get());
                    if (dto.getTitle() != null) post.setTitle(dto.getTitle());
                    if (dto.getContent() != null) post.setContent(dto.getContent());
                    if (dto.getStatus() != null) post.setStatus(postStatusRepository.findPostStatusByStatus(dto.getStatus()).get());
                    post.setUpdateTime(Instant.now());
                    return post;
                });
    }

    public Optional<Post> putPost(PostDto dto, Long id){
        return Optional
                .of(postRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(post -> {
                    post.setStatus(postStatusRepository.findPostStatusByStatus(dto.getStatus()).get());
                    post.setContent(dto.getContent());
                    post.setUser(userRepository.findById(dto.getUserId()).get());
                    post.setTitle(dto.getTitle());
                    post.setUpdateTime(Instant.now());
                    logger.debug("updated post: " +post.toString());
                    return post;
                });
    }

    public void deletePost(Long id){
        commentRepository.deleteAllByPost(postRepository.findById(id).get());
        postRepository.deleteById(id);
    }
    public byte[] exportPdf() {
        Document document = new Document();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph paragraph = new Paragraph("Blog Posts", new Font(Font.FontFamily.HELVETICA, 16.0f, Font.BOLD));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            PdfPCell cell = new PdfPCell();
            Font headFont = FontFactory.
                    getFont(FontFactory.HELVETICA_BOLD);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPaddingBottom(5f);
            cell.setBorderWidth(2);

            cell.setPhrase(new Phrase("Title", headFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Content", headFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Uesr", headFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Comments", headFont));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setPaddingLeft(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setLeading(1.2f, 1.2f);
            cell.setPaddingBottom(8f);
            Font tableContentFont = FontFactory.getFont("fonts/RobotoFlex.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            for (Post post : postRepository.findAll()){
                int commentsCount = commentRepository.countByPost(post).intValue();
                cell.setRowspan(commentsCount == 0 ? 1 : commentsCount);
                logger.error("post: " + post.getId() + " comments: " + commentRepository.countByPost(post).intValue());
                cell.setPhrase(new Phrase(post.getTitle(), tableContentFont));
                table.addCell(cell);
                cell.setPhrase(new Phrase(post.getContent(), tableContentFont));
                table.addCell(cell);
                cell.setPhrase(new Phrase(post.getUser().getFirstName() + ' ' + post.getUser().getLastName(), tableContentFont));
                table.addCell(cell);
                cell.setRowspan(1);
                if (commentRepository.existsByPost(post)){
                    for (Comment comment : commentRepository.findAllByPost(post)){
                        cell.setPhrase(new Phrase(comment.getContent(), tableContentFont));
                        table.addCell(cell);
                    }
                } else {
                    cell.setPhrase(new Phrase(""));
                    table.addCell(cell);
                }
            }
            document.add(table);
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image image = Image.getInstance(//CommentService.class.getClassLoader().getResourceAsStream("images/BgImage.png").readAllBytes());
                                            minioService.getObject("BgImage.png").readAllBytes());
            image.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            image.setAbsolutePosition(0, 0);
            canvas.addImage(image);
            document.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
