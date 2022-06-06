package com.blog.blog;

import com.blog.blog.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@EnableWebMvc
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.blog.blog"})
@EnableConfigurationProperties(AppConfiguration.class)
public class BlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}

