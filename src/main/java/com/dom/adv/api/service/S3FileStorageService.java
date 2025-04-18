package com.dom.adv.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service("s3FileStorageService")
public class S3FileStorageService implements FileStorageService{
    private final AmazonS3 amazonS3;
    @Value("aws.bucket-name")
    private String bucketName;

    public S3FileStorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a S3", e);
        }
    }
}
