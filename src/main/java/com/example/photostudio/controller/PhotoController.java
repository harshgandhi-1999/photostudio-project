package com.example.photostudio.controller;

import com.example.photostudio.dto.PhotoByTagListDto;
import com.example.photostudio.dto.PhotoUploadDto;
import com.example.photostudio.dto.PhotoUploadResponseDto;
import com.example.photostudio.dto.ResponseDto;
import com.example.photostudio.service.PhotoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photo")
@Validated
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PhotoUploadResponseDto> uploadPhoto(Authentication authentication, @Valid @ModelAttribute PhotoUploadDto photoUploadDto, @RequestParam Integer albumId) {
        String username = (String) authentication.getPrincipal();
        PhotoUploadResponseDto photoUploadResponseDto = photoService.uploadPhoto(username, photoUploadDto, albumId);
        return ResponseEntity.status(HttpStatus.CREATED).body(photoUploadResponseDto);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResponseDto> deletePhoto(Authentication authentication, @RequestParam Integer photoId) {
        String username = (String) authentication.getPrincipal();
        ResponseDto responseDto = photoService.deletePhoto(username, photoId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<PhotoByTagListDto> getAllPhotosByTag(@RequestParam @NotEmpty(message = "tag cannot be empty or null") String tag) {
        PhotoByTagListDto photos = photoService.getAllPhotosByTag(tag);
        return ResponseEntity.status(HttpStatus.OK).body(photos);
    }
}
