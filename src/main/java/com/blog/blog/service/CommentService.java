package com.blog.blog.service;

import com.blog.blog.BlogApplication;
import com.blog.blog.domain.Comment;
import com.blog.blog.repository.CommentRepository;
import com.blog.blog.repository.PostRepository;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.service.dto.CommentCsvDto;
import com.blog.blog.service.dto.CommentDto;
import com.blog.blog.service.dto.CommentXmlDto;
import com.blog.blog.service.dto.CommentXmlWrapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.opencsv.*;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MinioService minioService;
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);


    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, MinioService minioService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.minioService = minioService;
    }

    public Comment postComment(CommentDto dto){
        logger.debug("Creating new comment dto " + dto.toString());
        return commentRepository.save(new Comment(
                dto.getContent(),
                Instant.now(),
                userRepository.findById(dto.getUserId()).get(),
                postRepository.findById(dto.getPostId()).get()
        ));
    }

    public Optional<Comment> putComment(CommentDto dto, Long id){
        return Optional
                .of(commentRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(comment -> {
                    comment.setPost(postRepository.findById(dto.getPostId()).get());
                    comment.setUser(userRepository.findById(dto.getUserId()).get());
                    comment.setContent(dto.getContent());
                    logger.debug("updated comment: " + comment.toString());
                    return comment;
                });
    }

    public Optional<Comment> update(CommentDto dto, Long id){
        return Optional
                .of(commentRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(comment -> {
                    if (dto.getUserId() != null) comment.setUser(userRepository.findById(dto.getUserId()).get());
                    if (dto.getPostId() != null) comment.setPost(postRepository.findById(dto.getPostId()).get());
                    if (dto.getContent() != null) comment.setContent(dto.getContent());
                    logger.debug("updated comment: " + comment.toString());
                    return comment;
                });
    }

    public byte[] writeCommentsToCsv(){
        logger.debug("exporting comments to csv");
        List<CommentCsvDto> commentCsvDtoList = new ArrayList<CommentCsvDto>();
        for (Comment comment : commentRepository.findAll()){
            commentCsvDtoList.add(new CommentCsvDto(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreateTime(),
                    comment.getPost().getId(),
                    comment.getUser().getId()
                    ));
            }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (Writer out = new BufferedWriter(new OutputStreamWriter(baos))) {
                StatefulBeanToCsv<CommentCsvDto> writer = new StatefulBeanToCsvBuilder<CommentCsvDto>(out)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withEscapechar(CSVWriter.NO_ESCAPE_CHARACTER)
                        .withSeparator(';')
                        .withOrderedResults(false)
                        .build();
                writer.write(commentCsvDtoList);
            } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
                e.printStackTrace();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void importCsv(MultipartFile file) throws IOException, CsvValidationException {
        logger.debug("importing comments from csv");
        //commentRepository.deleteAll();
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        String[] comment;
        while ((comment = csvReader.readNext()) != null){
            //logger.error("post id: " + comment[4]);
            if (commentRepository.existsById(Long.parseLong(comment[0]))){
                logger.debug("addig comment " + comment.toString() + " from csv");
                commentRepository.save(new Comment(
                        comment[1],
                        Instant.parse(comment[2]),
                        userRepository.findById(Long.parseLong(comment[4])).get(),
                        postRepository.findById(Long.parseLong(comment[3])).get()
                ));
            }else {
                logger.error("comment with id " + comment[0] + " already exists");
            }
        }
        csvReader.close();
    }

    public ByteArrayInputStream toXml() throws JAXBException {
        logger.debug("exporting comments to xml");
        Marshaller marshaller = JAXBContext.newInstance(CommentXmlWrapper.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter xmlWriter = new StringWriter();
        marshaller.marshal(new CommentXmlWrapper(commentRepository.findAll()), xmlWriter);

        return new ByteArrayInputStream(xmlWriter.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void importXml(MultipartFile file) throws JAXBException, IOException {
        logger.debug("importing comments from xml");
        Unmarshaller unmarshaller = JAXBContext.newInstance(CommentXmlWrapper.class).createUnmarshaller();
        CommentXmlWrapper comments = (CommentXmlWrapper)unmarshaller.unmarshal(new InputStreamReader(file.getInputStream()));
        for (CommentXmlDto comment : comments.getComments()){
            commentRepository.save(new Comment(
                    comment.getContent(),
                    comment.getCreateTime().toGregorianCalendar().toInstant(),
                    userRepository.findById(comment.getUserId()).get(),
                    postRepository.findById(comment.getPostId()).get()
            ));
        }
    }

    public byte[] exportPdf() {
        Document document = new Document();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph paragraph = new Paragraph("Blog Comments", new Font(Font.FontFamily.HELVETICA, 16.0f, Font.BOLD));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            PdfPCell cell = new PdfPCell();
            Font headFont = FontFactory.
                    getFont(FontFactory.HELVETICA_BOLD);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPaddingBottom(5f);
            cell.setBorderWidth(2);

            cell.setPhrase(new Phrase("Content", headFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase("User", headFont));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Post", headFont));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setPaddingLeft(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setLeading(1.2f, 1.2f);
            cell.setPaddingBottom(8f);
            Font tableContentFont = FontFactory.getFont("fonts/RobotoFlex.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            for (Comment comment : commentRepository.findAll()){
                cell.setPhrase(new Phrase(comment.getContent(), tableContentFont));
                table.addCell(cell);
                cell.setPhrase(new Phrase(comment.getUser().getFirstName() + ' ' + comment.getUser().getLastName(), tableContentFont));
                table.addCell(cell);
                cell.setPhrase(new Phrase(comment.getPost().getTitle(), tableContentFont));
                table.addCell(cell);
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

