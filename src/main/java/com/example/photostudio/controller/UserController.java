package com.example.photostudio.controller;

import com.example.photostudio.dto.AlbumListDto;
import com.example.photostudio.dto.ProfileToUpdateDto;
import com.example.photostudio.dto.ResponseDto;
import com.example.photostudio.dto.UserProfileDto;
import com.example.photostudio.service.CloudinaryService;
import com.example.photostudio.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/hello")
    public String sayHello(Authentication authentication) {
        //System.out.println(authentication.getAuthorities());
        return "Hello world";
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        // for finding profile of any user
        UserProfileDto profile = userService.getUserProfile(username);

        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseDto> updateProfile(Authentication authentication, @Valid @RequestBody ProfileToUpdateDto newProfile) {
        String username = (String) authentication.getPrincipal();
        boolean profileUpdated = userService.updateUserProfile(username, newProfile);

        if (profileUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(), "Profile Updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), "Profile Updated Failed"));
        }
    }

    @GetMapping("/albums")
    public ResponseEntity<AlbumListDto> getAlbums(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        AlbumListDto albumListDto = userService.getAllUserAlbums(username);
        return ResponseEntity.status(HttpStatus.OK).body(albumListDto);
    }

}
