package com.example.photostudio.controller;

import com.example.photostudio.dto.AlbumRequestDto;
import com.example.photostudio.dto.AlbumResponseDto;
import com.example.photostudio.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping("/create")
    public ResponseEntity<AlbumResponseDto> createAlbum(@RequestBody AlbumRequestDto albumRequestDto, @RequestParam String username) {
        AlbumResponseDto albumResponseDto = albumService.createNewAlbum(albumRequestDto,username);

        return ResponseEntity.status(HttpStatus.OK).body(albumResponseDto);
    }
}
