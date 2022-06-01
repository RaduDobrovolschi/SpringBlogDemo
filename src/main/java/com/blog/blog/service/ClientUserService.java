package com.blog.blog.service;

import com.blog.blog.BlogApplication;
import com.blog.blog.config.AppConfiguration;
import com.blog.blog.domain.User;
import com.blog.blog.service.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;

@Service
public class ClientUserService {
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);
    private final RestTemplate restTemplate;
    private AppConfiguration.Connection connection;
    private final HttpHeaders httpHeaders;
    private final String connectionUrl;

    public ClientUserService(RestTemplate restTemplate, AppConfiguration configuration, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.connection = configuration.getConnection();
        this.httpHeaders = httpHeaders;
        connectionUrl = connection.getServerIp() + "/api/user";
        httpHeaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString((connection.getUsername() + ':' + connection.getPassword()).getBytes()));
    }

    public List<User> getAllUsers(){
        logger.debug("requesting Posts from " + connectionUrl);
        try {
            return restTemplate.exchange(
                connectionUrl,
                HttpMethod.GET,
                new HttpEntity(httpHeaders),
                new ParameterizedTypeReference<List<User>>() {}
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public User getUser(Long id){
        logger.debug("getting user no + " + id + " from " + connectionUrl);
        try {
            return restTemplate.exchange(
                connectionUrl,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                User.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }

    public void deleteUser(Long id){
        logger.debug("deleting User no: " + id + " from " + connectionUrl);
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

    public User postUser(UserDto dto){
        logger.debug("posting user {}: " + dto.toString());
        try {
            return restTemplate.exchange(
                connectionUrl,
                HttpMethod.POST,
                new HttpEntity(dto, httpHeaders),
                User.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }


    public User putUser(UserDto dto, Long id){
        logger.debug("putting comment {} " + dto.toString() + "to url " + connectionUrl + '/' + id.toString());
        try {
            return restTemplate.exchange(
                connectionUrl + '/' + id.toString(),
                HttpMethod.PUT,
                new HttpEntity(dto, httpHeaders),
                User.class
        ).getBody();
        } catch (HttpClientErrorException ex){
            logger.error("Response error with status code " + ex.getStatusCode());
            throw new ResponseStatusException(ex.getStatusCode());
        }
    }
}
