package com.example.photostudio.controller;

import com.example.photostudio.dto.*;
import com.example.photostudio.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PhotoUploadResponseDto> uploadPhoto(@ModelAttribute PhotoUploadDto photoUploadDto, @RequestParam Integer albumId, @RequestParam String username) {
        PhotoUploadResponseDto photoUploadResponseDto = photoService.uploadPhoto(photoUploadDto, albumId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(photoUploadResponseDto);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResponseDto> deletePhoto(@RequestParam Integer photoId, @RequestParam String username) {
        ResponseDto responseDto = photoService.deletePhoto(photoId, username);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<PhotoByTagListDto> getAllPhotosByTag(@RequestParam String tag) {
        PhotoByTagListDto photos = photoService.getAllPhotosByTag(tag);
        return ResponseEntity.status(HttpStatus.OK).body(photos);
    }
}
