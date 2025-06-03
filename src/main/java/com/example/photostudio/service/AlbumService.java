package com.example.photostudio.service;

import com.example.photostudio.dto.*;
import org.springframework.security.core.Authentication;

public interface AlbumService {
    AlbumResponseDto createNewAlbum(String username, AlbumRequestDto albumRequestDto);

    AlbumResponseDto updateAlbum(String username, AlbumDto albumDto);

    ResponseDto deleteAlbum(String username, Integer albumId);

    PhotoListDto getAllPhotos(String username, Integer albumId);
}
