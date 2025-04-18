package com.dom.adv.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3UploadService implements UploadService {

    private final AmazonS3 amazonS3;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.endpoint}")
    private String endpointUrl;

    public S3UploadService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String key = "images/" + UUID.randomUUID() + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(new PutObjectRequest(
                bucketName,
                key,
                multipartFile.getInputStream(),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead));

        return String.format("%s/%s/%s", endpointUrl, bucketName, key);
    }
}
