package com.example.photostudio.service;

import com.example.photostudio.dto.AlbumRequestDto;
import com.example.photostudio.dto.AlbumResponseDto;

public interface AlbumService {
    AlbumResponseDto createNewAlbum(AlbumRequestDto albumRequestDto, String username);
}
