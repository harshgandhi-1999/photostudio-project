package com.example.photostudio.service.impl;

import com.example.photostudio.dto.AlbumListDto;
import com.example.photostudio.dto.UserProfileDto;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.exception.UserAlreadyExistException;
import com.example.photostudio.mapper.UserMapper;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final Logger logger = Logger.getLogger(UserService.class.getName());

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
    public boolean updateUserProfile(String username, UserProfileDto newProfile) {
        logger.info("UPDATE_USER_PROFILE");
        Optional<User> currentUser = userRepository.findByUsername(username);

        if (currentUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        //check if new profile username already is taken by some other
        // if exist then we can't set username
        Optional<User> existingUser = userRepository.findByUsername(newProfile.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException(newProfile.getUsername());
        }

        User.UserBuilder userBuilder = currentUser.get().toBuilder();

        User updatedUser = userBuilder
                .username(newProfile.getUsername())
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .build();

        logger.info("Updated user = " + updatedUser);

        userRepository.save(updatedUser);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(optionalUser.get().getUsername())
                .password(optionalUser.get().getPassword())
                .build();
    }

    @Override
    public AlbumListDto getAllUserAlbums(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return userMapper.userToAlbumListDto(optionalUser.get());
    }
}
