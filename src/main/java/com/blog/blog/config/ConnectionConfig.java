package com.blog.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

@Configuration
@ConfigurationProperties(prefix = "connection")
public class ConnectionConfig {
    private String ServerIp;
    private String username;
    private String password;

    public String getServerIp() {
        return ServerIp;
    }

    public void setServerIp(String serverIp) {
        ServerIp = serverIp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
