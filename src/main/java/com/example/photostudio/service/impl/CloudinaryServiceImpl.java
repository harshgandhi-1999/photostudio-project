package com.example.photostudio.service.impl;

import com.cloudinary.Cloudinary;
import com.example.photostudio.dto.CloudImageDto;
import com.example.photostudio.service.CloudinaryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    private final Logger logger = Logger.getLogger(CloudinaryService.class.getName());

    @Override
    public CloudImageDto upload(MultipartFile file) {
        try {
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "my-gallery");
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            String url = cloudinary.url().secure(true).generate(publicId);
            logger.info("URL = " + url);
            return new CloudImageDto(url, publicId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean delete(String publicId) {
        try {
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "my-gallery");
            cloudinary.uploader().destroy(publicId, options);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
