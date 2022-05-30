package com.blog.blog.service;

import com.blog.blog.service.dto.FileDto;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public InputStream getObject(String filename) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("Error happened when get list objects from minio: ", e);
            return null;
        }
        return stream;
    }


    public List<FileDto> getListObjects() {
        List<FileDto> objects = new ArrayList<>();
        try {
            for (Result<Item> item : minioClient.listObjects(ListObjectsArgs.builder()
                                    .bucket(bucketName)
                                    .recursive(true)
                                    .build())) {
                objects.add(FileDto.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName()))
                        .build());
            }
            return objects;
        } catch (Exception e) {
            log.error("Error happened when get list objects from minio: ", e);
        }
        return objects;
    }

    private String getPreSignedUrl(String filename) {
        return "http://localhost:8080/minio/".concat(filename);
    }

}
