package com.example.photostudio.service;

import com.example.photostudio.dto.*;

public interface AlbumService {
    AlbumResponseDto createNewAlbum(AlbumRequestDto albumRequestDto, String username);

    AlbumResponseDto updateAlbum(AlbumDto albumDto, String username);

    ResponseDto deleteAlbum(Integer albumId, String username);

    PhotoListDto getAllPhotos(Integer albumId, String username);
}
