package com.blog.blog.service;

import com.blog.blog.BlogApplication;
import com.blog.blog.config.ConnectionConfig;
import com.blog.blog.domain.Post;
import com.blog.blog.service.dto.PostDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;

@Service
public class ClientPostService {
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);
    private final RestTemplate restTemplate;
    private ConnectionConfig connectionConfig;
    private final HttpHeaders httpHeaders;
    private final String connectionUrl;

    public ClientPostService(RestTemplate restTemplate, ConnectionConfig connectionConfig, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.connectionConfig = connectionConfig;
        this.httpHeaders = httpHeaders;
        connectionUrl = connectionConfig.getServerIp() + "/api/posts";
        httpHeaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString((connectionConfig.getUsername() + ':' + connectionConfig.getPassword()).getBytes()));
    }

    public List<Post> getAllPosts(){
        logger.debug("requesting Posts from " + connectionUrl);
        try {
            return restTemplate.exchange(
                connectionUrl,
                HttpMethod.GET,
                new HttpEntity(httpHeaders),
                new ParameterizedTypeReference<List<Post>>(){}
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public Post getPost(Long id){
        logger.debug("getting Post no " + id + " from " + connectionUrl);
        try {
            return  restTemplate.exchange(
                connectionUrl + '/' + id.toString(),
                HttpMethod.GET,
                new HttpEntity(httpHeaders),
                Post.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public Post postPost(PostDto dto) throws URISyntaxException {
        logger.debug("posting Post {}: " + dto.toString());
        try {
            return restTemplate.exchange(
                connectionUrl,
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                Post.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public void deletePost(Long id){
        logger.debug("deleting post no: " + id + " from " + connectionUrl);
        try{
            restTemplate.exchange(
                connectionUrl + '/' + id.toString(),
                HttpMethod.DELETE,
                new HttpEntity(httpHeaders),
                Void.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public Post putPost(PostDto dto, Long id){
        logger.debug("putting comment {} " + dto.toString() + "to url " +  connectionUrl + '/' + id.toString());
        try {
            return restTemplate.exchange(
                connectionUrl + '/' + id.toString(),
                HttpMethod.PUT,
                new HttpEntity(dto, httpHeaders),
                Post.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }
}
