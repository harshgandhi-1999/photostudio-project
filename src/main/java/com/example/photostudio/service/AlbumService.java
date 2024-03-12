package com.example.photostudio.service;

import com.example.photostudio.dto.*;
import org.springframework.security.core.Authentication;

public interface AlbumService {
    AlbumResponseDto createNewAlbum(Authentication authentication, AlbumRequestDto albumRequestDto);

    AlbumResponseDto updateAlbum(Authentication authentication, AlbumDto albumDto);

    ResponseDto deleteAlbum(Authentication authentication, Integer albumId);

    PhotoListDto getAllPhotos(Authentication authentication, Integer albumId);
}
