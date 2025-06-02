package com.example.photostudio.service;

import com.example.photostudio.dto.AlbumListDto;
import com.example.photostudio.dto.ProfileToUpdateDto;
import com.example.photostudio.dto.UserProfileDto;

public interface UserService {
    UserProfileDto getUserProfile(String username);

    boolean updateUserProfile(String username, ProfileToUpdateDto userProfileDto);

    AlbumListDto getAllUserAlbums(String username);
}
