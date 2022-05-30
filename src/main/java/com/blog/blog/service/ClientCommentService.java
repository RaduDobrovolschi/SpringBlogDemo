package com.blog.blog.service;

import com.blog.blog.BlogApplication;
import com.blog.blog.config.ConnectionConfig;
import com.blog.blog.domain.Comment;
import com.blog.blog.service.dto.CommentDto;
import com.blog.blog.service.dto.CommentXmlDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Base64;
import java.util.List;

@Service
public class ClientCommentService {
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);
    private final RestTemplate restTemplate;
    private ConnectionConfig connectionConfig;
    private final HttpHeaders httpHeaders;
    private final String connectionUrl;

    public ClientCommentService(RestTemplate restTemplate, ConnectionConfig connectionConfig, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.connectionConfig = connectionConfig;
        this.httpHeaders = httpHeaders;
        connectionUrl = connectionConfig.getServerIp() + "/api/comments";
        httpHeaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString((connectionConfig.getUsername() + ':' + connectionConfig.getPassword()).getBytes()));
    }

    public List<Comment> getAllComments() {
        logger.debug("requesting comments from " + connectionUrl);
        try {
            return restTemplate.exchange(
                    connectionUrl,
                    HttpMethod.GET,
                    new HttpEntity(httpHeaders),
                    new ParameterizedTypeReference<List<Comment>>() {}
            ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }

    }

    public Comment getComment(Long id) {
        logger.debug("getting comment no " + id + " from " + connectionConfig.getServerIp());
        try {
            return restTemplate.exchange(
                    connectionUrl + '/' + id.toString(),
                    HttpMethod.GET, new HttpEntity(httpHeaders),
                    Comment.class
            ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public Comment postComment(CommentDto dto){
        logger.debug("posting comment {}: " + dto.toString());
        try {
            return restTemplate.exchange(
                    connectionUrl,
                    HttpMethod.POST,
                    new HttpEntity(dto, httpHeaders),
                    Comment.class
            ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public void deleteComment(Long id) {
        logger.debug("deleting comment no: " + id + " from " + connectionUrl);
        try {
            restTemplate.exchange(
                    connectionUrl + '/' + id.toString(),
                    HttpMethod.DELETE,
                    new HttpEntity(httpHeaders),
                    Void.class
            );
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public Comment putComment(CommentDto dto, Long id) {
        logger.debug("putting comment {} " + dto.toString() + "to url " + connectionUrl + '/' + id.toString());
        try {
            return restTemplate.exchange(
                connectionUrl + '/' + id.toString(),
                HttpMethod.PUT,
                new HttpEntity(dto, httpHeaders),
                Comment.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }
}