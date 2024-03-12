package com.example.photostudio.service;

import com.example.photostudio.dto.*;
import org.springframework.security.core.Authentication;

public interface PhotoService {
    PhotoUploadResponseDto uploadPhoto(Authentication authentication, PhotoUploadDto photoUploadDto, Integer albumId);

    ResponseDto deletePhoto(Authentication authentication, Integer photoId);

    PhotoByTagListDto getAllPhotosByTag(String tag);
}
