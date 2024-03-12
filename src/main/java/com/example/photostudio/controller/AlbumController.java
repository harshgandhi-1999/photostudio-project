package com.example.photostudio.controller;

import com.example.photostudio.dto.*;
import com.example.photostudio.service.AlbumService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/album")
@Validated
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping("/create")
    public ResponseEntity<AlbumResponseDto> createAlbum(Authentication authentication, @Valid @RequestBody AlbumRequestDto albumRequestDto) {
        AlbumResponseDto albumResponseDto = albumService.createNewAlbum(authentication, albumRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(albumResponseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<AlbumResponseDto> updateAlbum(Authentication authentication, @Valid @RequestBody AlbumDto albumDto) {
        AlbumResponseDto albumResponseDto = albumService.updateAlbum(authentication, albumDto);

        return ResponseEntity.status(HttpStatus.OK).body(albumResponseDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAlbum(Authentication authentication, @RequestParam Integer albumId) {
        ResponseDto responseDto = albumService.deleteAlbum(authentication, albumId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/photos")
    public ResponseEntity<PhotoListDto> getPhotos(Authentication authentication, @RequestParam Integer albumId) {
        PhotoListDto photoListDto = albumService.getAllPhotos(authentication, albumId);
        return ResponseEntity.status(HttpStatus.OK).body(photoListDto);
    }
}
