package com.dom.adv.api.service.aws;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    String uploadImage(MultipartFile file) throws IOException;
}
