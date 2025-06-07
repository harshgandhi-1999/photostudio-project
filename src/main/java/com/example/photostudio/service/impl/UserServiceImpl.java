package com.example.photostudio.service.impl;

import com.example.photostudio.dto.AlbumListDto;
import com.example.photostudio.dto.ProfileToUpdateDto;
import com.example.photostudio.dto.UserProfileDto;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.mapper.UserMapper;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return optionalUser.get();
    }

    @Override
    public UserProfileDto getUserProfile(String username) {
        logger.info("GET_USER_PROFILE");
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        return userMapper.userToUserProfileDto(optionalUser.get());
    }

    @Override
    public boolean updateUserProfile(String username, ProfileToUpdateDto newProfile) {
        logger.info("UPDATE_USER_PROFILE");

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        User currentUser = optionalUser.get();

        if (newProfile.getName() != null) {
            currentUser.setName(newProfile.getName());
        }
        if (newProfile.getEmail() != null) {
            currentUser.setEmail(newProfile.getEmail());
        }

        userRepository.save(currentUser);

        return true;
    }

    @Override
    public AlbumListDto getAllUserAlbums(String username) {
        logger.info("GET ALL USER ALBUMS");
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return userMapper.userToAlbumListDto(optionalUser.get());
    }
}
