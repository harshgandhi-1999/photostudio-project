package com.example.photostudio.service;

import com.example.photostudio.dto.*;

public interface PhotoService {
    PhotoUploadResponseDto uploadPhoto(PhotoUploadDto photoUploadDto, Integer albumId, String username);

    ResponseDto deletePhoto(Integer photoId, String username);

    PhotoByTagListDto getAllPhotosByTag(String tag);
}
