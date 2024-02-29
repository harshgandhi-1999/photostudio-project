package com.example.photostudio.service;

import com.example.photostudio.dto.AlbumDto;
import com.example.photostudio.dto.AlbumRequestDto;
import com.example.photostudio.dto.AlbumResponseDto;
import com.example.photostudio.dto.ResponseDto;

public interface AlbumService {
    AlbumResponseDto createNewAlbum(AlbumRequestDto albumRequestDto, String username);

    AlbumResponseDto updateAlbum(AlbumDto albumDto, String username);

    ResponseDto deleteAlbum(Integer albumId, String username);
}
