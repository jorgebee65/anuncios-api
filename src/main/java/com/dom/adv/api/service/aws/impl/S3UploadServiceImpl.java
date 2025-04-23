package com.dom.adv.api.service.aws.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dom.adv.api.service.aws.UploadService;
import com.dom.adv.api.util.FileNameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Profile("aws")
public class S3UploadServiceImpl implements UploadService {

    private final AmazonS3 amazonS3;
    private final FileNameGenerator fileNameGenerator;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.endpoint}")
    private String endpointUrl;

    public S3UploadServiceImpl(AmazonS3 amazonS3, FileNameGenerator fileNameGenerator) {
        this.amazonS3 = amazonS3;
        this.fileNameGenerator = fileNameGenerator;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String key = fileNameGenerator.generate(multipartFile.getOriginalFilename());

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
