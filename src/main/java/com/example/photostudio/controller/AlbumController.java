package com.example.photostudio.controller;

import com.example.photostudio.dto.*;
import com.example.photostudio.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AlbumResponseDto albumResponseDto = albumService.createNewAlbum(userDetails.getUsername(), albumRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(albumResponseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<AlbumResponseDto> updateAlbum(Authentication authentication, @Valid @RequestBody AlbumDto albumDto) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AlbumResponseDto albumResponseDto = albumService.updateAlbum(userDetails.getUsername(), albumDto);

        return ResponseEntity.status(HttpStatus.OK).body(albumResponseDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAlbum(Authentication authentication, @RequestParam Integer albumId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        ResponseDto responseDto = albumService.deleteAlbum(userDetails.getUsername(), albumId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/photos")
    public ResponseEntity<PhotoListDto> getPhotos(Authentication authentication, @RequestParam Integer albumId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        PhotoListDto photoListDto = albumService.getAllPhotos(userDetails.getUsername(), albumId);
        return ResponseEntity.status(HttpStatus.OK).body(photoListDto);
    }
}
