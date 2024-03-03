package com.example.photostudio.service;

import com.example.photostudio.dto.PhotoUploadDto;
import com.example.photostudio.dto.PhotoUploadResponseDto;
import com.example.photostudio.dto.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    PhotoUploadResponseDto uploadPhoto(PhotoUploadDto photoUploadDto, Integer albumId, String username);
}
