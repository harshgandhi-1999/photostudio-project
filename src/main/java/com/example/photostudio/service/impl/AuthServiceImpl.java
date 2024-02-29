package com.example.photostudio.service.impl;

import com.example.photostudio.dto.*;
import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.NotAuthorizedException;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.exception.UserAlreadyExistException;
import com.example.photostudio.mapper.UserMapper;
import com.example.photostudio.repository.AlbumRepository;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.AuthService;
import com.example.photostudio.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper;

    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        logger.info("AUTHENTICATE USER SERVICE");
        // check if user exists or not
        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDto.getUsername());

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", loginRequestDto.getUsername());
        }

        User user = optionalUser.get();
        UserProfileDto userDetails = userMapper.userToUserProfileDto(user);

        //check if password match
        if (!Objects.equals(user.getPassword(), loginRequestDto.getPassword())) {
            throw new NotAuthorizedException("Password is incorrect");
        }

        // generate token
        String token = jwtTokenProvider.generateToken(userDetails.getUsername());
        logger.info("Token: " + token);
        return new LoginResponseDto(userDetails, token);
    }


    @Override
    public void createUser(SignupRequestDto signupRequestDto) {
        logger.info("CREATE USER SERVICE");
        // check if user exists or not
        Optional<User> optionalUser = userRepository.findByUsername(signupRequestDto.getUsername());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException(signupRequestDto.getUsername());
        }


        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .password(signupRequestDto.getPassword())
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .build();
        // create default album
        Album album = Album.builder()
                        .albumName("default")
                        .user(user)
                        .build();
        user.setAlbums(List.of(album));

        logger.info("ALBUM = " + album.toString());
        logger.info("USER = " + user.toString());

        // save in database
        userRepository.save(user);
    }
}
