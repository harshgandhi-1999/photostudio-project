package com.example.photostudio.service;

import com.example.photostudio.dto.CloudImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    CloudImageDto upload(MultipartFile file);

    boolean delete(String publicId);
}
