package com.example.photostudio.service.impl;

import com.example.photostudio.dto.*;
import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.AuthenticationFailedException;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.exception.UserAlreadyExistException;
import com.example.photostudio.mapper.UserMapper;
import com.example.photostudio.repository.AlbumRepository;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.AuthService;
import com.example.photostudio.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder encoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        logger.info("AUTHENTICATE USER SERVICE");

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
            logger.info("Principal = " + authentication.getPrincipal() + " \n Credentials = " + authentication.getCredentials());
            if (authentication.isAuthenticated()) {
                logger.info("AUTHENTICATED");
            } else {
                throw new AuthenticationFailedException("Username or Password is incorrect");
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

            if (optionalUser.isEmpty()) {
                throw new ResourceNotFoundException("User", "username", userDetails.getUsername());
            }

            User user = optionalUser.get();

            UserProfileDto userProfileDto = UserProfileDto.builder()
                    .name(user.getName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();

            // generate token
            String token = jwtTokenProvider.generateToken(userDetails.getUsername());
            //logger.info("Token: " + token);
            return new LoginResponseDto(userProfileDto, token);
        } catch (AuthenticationException exc) {
            // if username or password is incorrect then exception will be raised
            throw new AuthenticationFailedException("Username or Password is incorrect");
        }
    }


    @Override
    public void createUser(SignupRequestDto signupRequestDto) {
        logger.info("CREATE USER SERVICE");
        // check if user exists or not
        Optional<User> optionalUser = userRepository.findByUsername(signupRequestDto.getUsername());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException(signupRequestDto.getUsername());
        }

        // encode the password
        String encodedPassword = encoder.encode(signupRequestDto.getPassword());
        //logger.info("Encoded Password: " + encodedPassword);
        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .password(encodedPassword)
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .build();
        // create default album
        Album album = Album.builder()
                .albumName("default")
                .user(user)
                .build();
        user.setAlbums(List.of(album));

        // save in database
        userRepository.save(user);
    }
}
