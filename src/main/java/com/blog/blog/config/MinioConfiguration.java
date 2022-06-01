package com.blog.blog.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MinioConfiguration {

    private final AppConfiguration.Minio minio;
    public MinioConfiguration(AppConfiguration configuration) {
        this.minio = configuration.getMinio();
    }

    @Bean
    @Primary
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .credentials(minio.getKey(), minio.getSecret())
                .endpoint(minio.getUrl())
                .build();
    }
}
