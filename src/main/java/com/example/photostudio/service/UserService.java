package com.example.photostudio.service;

import com.example.photostudio.dto.UserProfileDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserProfileDto getUserByUsername(String username);
}
