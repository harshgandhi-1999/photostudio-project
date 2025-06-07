package com.example.photostudio.controller;

import com.example.photostudio.dto.*;
import com.example.photostudio.service.AlbumService;
import jakarta.validation.Valid;
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
        String username = (String) authentication.getPrincipal();
        AlbumResponseDto albumResponseDto = albumService.createNewAlbum(username, albumRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(albumResponseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<AlbumResponseDto> updateAlbum(Authentication authentication, @Valid @RequestBody AlbumDto albumDto) {
        String username = (String) authentication.getPrincipal();
        AlbumResponseDto albumResponseDto = albumService.updateAlbum(username, albumDto);

        return ResponseEntity.status(HttpStatus.OK).body(albumResponseDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAlbum(Authentication authentication, @RequestParam Integer albumId) {
        String username = (String) authentication.getPrincipal();
        ResponseDto responseDto = albumService.deleteAlbum(username, albumId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/photos")
    public ResponseEntity<PhotoListDto> getPhotos(Authentication authentication, @RequestParam Integer albumId) {
        String username = (String) authentication.getPrincipal();
        PhotoListDto photoListDto = albumService.getAllPhotos(username, albumId);
        return ResponseEntity.status(HttpStatus.OK).body(photoListDto);
    }
}
