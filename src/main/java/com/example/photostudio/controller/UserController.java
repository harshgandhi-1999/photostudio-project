package com.example.photostudio.controller;

import com.example.photostudio.dto.AlbumListDto;
import com.example.photostudio.dto.ResponseDto;
import com.example.photostudio.dto.UserProfileDto;
import com.example.photostudio.service.CloudinaryService;
import com.example.photostudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello world";
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@RequestParam String username) {

        UserProfileDto profile = userService.getUserProfile(username);

        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ResponseDto> updateProfile(@RequestBody UserProfileDto newProfile, @RequestParam String username) {
        boolean profileUpdated = userService.updateUserProfile(username, newProfile);

        if (profileUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(), "Profile Updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), "Profile Updated Failed"));
        }
    }

    @GetMapping("/albums")
    public ResponseEntity<AlbumListDto> getAlbums(@RequestParam String username) {
        AlbumListDto albumListDto = userService.getAllUserAlbums(username);
        return ResponseEntity.status(HttpStatus.OK).body(albumListDto);
    }

}
