package com.example.photostudio.service;

import com.example.photostudio.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    PhotoUploadResponseDto uploadPhoto(PhotoUploadDto photoUploadDto, Integer albumId, String username);

    List<PhotoByTagDto> getAllPhotosByTag(String tag);
}
