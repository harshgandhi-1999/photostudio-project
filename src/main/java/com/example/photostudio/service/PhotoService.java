package com.example.photostudio.service;

import com.example.photostudio.dto.PhotoByTagListDto;
import com.example.photostudio.dto.PhotoUploadDto;
import com.example.photostudio.dto.PhotoUploadResponseDto;
import com.example.photostudio.dto.ResponseDto;

public interface PhotoService {
    PhotoUploadResponseDto uploadPhoto(String username, PhotoUploadDto photoUploadDto, Integer albumId);

    ResponseDto deletePhoto(String username, Integer photoId);

    PhotoByTagListDto getAllPhotosByTag(String tag);
}
