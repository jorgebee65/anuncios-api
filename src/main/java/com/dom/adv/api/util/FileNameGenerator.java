package com.dom.adv.api.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileNameGenerator {
    public String generate(String originalFilename) {
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        return "images/" + UUID.randomUUID() + extension;
    }
}