package com.example.photostudio.service;

import com.example.photostudio.dto.AlbumListDto;
import com.example.photostudio.dto.ProfileToUpdateDto;
import com.example.photostudio.dto.UserProfileDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserProfileDto getUserProfile(String username);

    boolean updateUserProfile(Authentication authentication, ProfileToUpdateDto userProfileDto);

    AlbumListDto getAllUserAlbums(Authentication authentication);
}
